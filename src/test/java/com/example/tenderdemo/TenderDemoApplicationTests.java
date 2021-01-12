package com.example.tenderdemo;

import com.example.tenderdemo.model.Offer;
import com.example.tenderdemo.model.Status;
import com.example.tenderdemo.model.Tender;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenderDemoApplicationTests {

    @LocalServerPort()
    private int port = 8080;

    @Autowired()
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    final String baseUrl = "http://localhost:" + port + "/v1";

    private static Tender tender;
    private static String acceptedOfferId;
    private static Random random = new Random();
    private static String bidder1 = "Top-Construction";
    private static String bidder2 = "Great-Construction";

    @Test
    @Order(1)
    public void shouldCreateTender() throws URISyntaxException {

        tender = new Tender();
        tender.setDescription("Build a nice hospital");
        tender.setIssuer("Alpha-City");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Tender> request = new HttpEntity<>(tender, headers);
        URI uri = new URI(baseUrl + "/tenders");

        ResponseEntity<Tender> result = restTemplate.exchange(uri, HttpMethod.POST, request, Tender.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertNotNull(result.getBody().getDescription());
        Assert.assertNotNull(result.getBody().getIssuer());
        tender.setId(result.getBody().getId());
    }

    @Test
    @Order(2)
    public void shouldCreateOffer() throws URISyntaxException {

        Offer offer1 = new Offer();
        offer1.setBidder(bidder1);
        Offer offer2 = new Offer();
        offer2.setBidder(bidder1);
        Offer offer3 = new Offer();
        offer3.setBidder(bidder2);
        List<Offer> offers = new ArrayList<>();
        offers.add(offer1);
        offers.add(offer2);
        offers.add(offer3);
        int i = 1;
        for (Offer o : offers) {
            String tenderId = tender.getId();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Offer> request = new HttpEntity<>(o, headers);
            URI uri = new URI(baseUrl + "/tenders/" + tenderId + "/offers");
            ResponseEntity<Tender> result = restTemplate.exchange(uri, HttpMethod.POST, request, Tender.class);
            Assert.assertEquals(200, result.getStatusCodeValue());
            Assert.assertEquals(i, result.getBody().getOffers().size());
            i++;
            tender.setOffers(result.getBody().getOffers());
        }
    }

    @Test
    @Order(3)
    public void shouldUpdateOffer() throws URISyntaxException {

        String tenderId = tender.getId();
        int randomOffersIndex = random.nextInt(tender.getOffers().size());

        acceptedOfferId = tender.getOffers().get(randomOffersIndex).getId();
        Offer offer = new Offer();
        offer.setStatus(Status.ACCEPTED);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Offer> request = new HttpEntity<>(offer, headers);

        URI uri = new URI(baseUrl + "/tenders/" + tenderId + "/offers/" + acceptedOfferId);

        ResponseEntity<Tender> result = restTemplate.exchange(uri, HttpMethod.PATCH, request, Tender.class);
        Assert.assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().getOffers().stream().anyMatch(o ->
                acceptedOfferId.equals(o.getId()) ? o.getStatus().equals(Status.ACCEPTED) : o.getStatus().equals(Status.REJECTED)));
        tender.setOffers(result.getBody().getOffers());
    }

    @Test
    @Order(3)
    public void shouldNotRejectAcceptedOffer() throws URISyntaxException {

        String tenderId = tender.getId();
        Offer offer = new Offer();
        offer.setStatus(Status.REJECTED);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Offer> request = new HttpEntity<>(offer, headers);

        URI uri = new URI(baseUrl + "/tenders/" + tenderId + "/offers/" + acceptedOfferId);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PATCH, request, String.class);
        Assert.assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    @Order(4)
    public void shouldReturnOffersForOneTender() throws URISyntaxException {
        String tenderId = tender.getId();
        URI uri = new URI(baseUrl + "/tenders/" + tenderId + "/offers");

        Offer offer = new Offer();
        offer.setStatus(Status.REJECTED);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<List<Offer>> result = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<Offer>>() {
        });

        List<Offer> existingOffers = tender.getOffers();
        List<Offer> responseOffers = result.getBody();

        Assert.assertEquals(200, result.getStatusCodeValue());
        assertTrue(existingOffers.size() == responseOffers.size() &&
                existingOffers.containsAll(responseOffers) && responseOffers.containsAll(existingOffers));
    }

    @Test
    @Order(4)
    public void shouldReturnOffersByBidder() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/offers")
                .queryParam("bidder", bidder1);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<List<Offer>> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, new ParameterizedTypeReference<List<Offer>>() {
        });
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertTrue(result.getBody().stream().allMatch(t -> t.getBidder().equals(bidder1)));
    }

    @Test
    @Order(4)
    public void shouldReturnOffersByBidderForTender() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/offers")
                .queryParam("tenderId", tender.getId())
                .queryParam("bidder", bidder1);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<List<Offer>> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, new ParameterizedTypeReference<List<Offer>>() {
        });
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertTrue(result.getBody().stream().allMatch(t -> t.getBidder().equals(bidder1)));
    }


    @Test
    @Order(4)
    public void shouldReturnTendersByIssuer() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/tenders")
                .queryParam("issuer", tender.getIssuer());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<List<Tender>> result = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, request, new ParameterizedTypeReference<List<Tender>>() {
                });
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertTrue(result.getBody().stream().allMatch(t -> t.getIssuer().equals(tender.getIssuer())));

    }
}
