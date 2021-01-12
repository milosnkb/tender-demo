package com.example.tenderdemo.controller;

import com.example.tenderdemo.model.Offer;
import com.example.tenderdemo.model.Status;
import com.example.tenderdemo.model.Tender;
import com.example.tenderdemo.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * Rest controller class that handles the requests
 */
@RestController
@RequestMapping("/v1/tenders")
public class TenderController {
    @Autowired
    private TenderService tenderService;

    /**
     * Create tender response entity.
     *
     * @param tender the tender
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity createTender(@Valid @RequestBody Tender tender) {
        tender.setOffers(Collections.emptyList());
        tenderService.createTender(tender);
        return new ResponseEntity<>(tender, HttpStatus.OK);
    }

    /**
     * Add offer response entity.
     *
     * @param offer    the offer
     * @param tenderId the tender id
     * @return the response entity
     */
    @PostMapping("/{tenderId}/offers")
    public ResponseEntity addOffer(@Valid @RequestBody Offer offer, @PathVariable("tenderId") String tenderId) {
        Tender tender = tenderService.addOfferToTender(tenderId, offer);
        return new ResponseEntity<>(tender, HttpStatus.OK);
    }

    /**
     * Update offer response entity.
     *
     * @param offer    the offer
     * @param tenderId the tender id
     * @param offerId  the offer id
     * @return the response entity
     */
    @PatchMapping("/{tenderId}/offers/{offerId}")
    public ResponseEntity updateOffer(@RequestBody Offer offer, @PathVariable("tenderId") String tenderId,
                                      @PathVariable("offerId") String offerId) {
        offer.setId(offerId);
        Tender tender = null;
        if (offer.getStatus().equals(Status.ACCEPTED)) {
            tender = tenderService.acceptOffer(tenderId, offer);
            return new ResponseEntity<>(tender, HttpStatus.OK);
        }
        return new ResponseEntity<>(tender, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets offers for tender.
     *
     * @param tenderId the tender id
     * @return the offers for tender
     */
    @GetMapping("/{tenderId}/offers")
    public ResponseEntity getOffersForTender(@PathVariable("tenderId") String tenderId) {
        List<Offer> offers = tenderService.getOffersForTender(tenderId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    /**
     * Gets offers for issuer.
     *
     * @param issuer the issuer
     * @return the offers for issuer
     */
    @GetMapping()
    public ResponseEntity getOffersForIssuer(@RequestParam String issuer) {
        List<Tender> tenders = tenderService.getTendersByIssuer(issuer);
        return new ResponseEntity<>(tenders, HttpStatus.OK);
    }

}
