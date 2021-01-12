package com.example.tenderdemo.service;

import com.example.tenderdemo.model.Offer;
import com.example.tenderdemo.model.Status;
import com.example.tenderdemo.model.Tender;
import com.example.tenderdemo.repository.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service layer that performs service logic
 */
@Service
public class TenderService {

    @Autowired
    private TenderRepository tenderRepository;

    /**
     * Create tender tender.
     *
     * @param tender the tender
     * @return the tender
     */
    public Tender createTender(Tender tender) {
        return tenderRepository.save(tender);
    }

    /**
     * Gets tender.
     *
     * @param id the id
     * @return tender
     */
    public Optional<Tender> getTender(String id) {
        Optional<Tender> optional = tenderRepository.findById(id);
        return optional;
    }


    /**
     * Add offer to tender tender.
     *
     * @param tenderId the tender id
     * @param offer    the offer
     * @return the tender
     */
    public Tender addOfferToTender(String tenderId, Offer offer) {
        Tender tender = getTender(tenderId).orElse(null);
        if (tender != null) {
            offer.setStatus(Status.PENDING);
            offer.setId(UUID.randomUUID().toString());
            tender.getOffers().add(offer);
        }
        return createTender(tender);
    }


    /**
     * Accept offer tender.
     *
     * @param tenderId the tender id
     * @param offer    the offer
     * @return the tender
     * @throws Exception the exception
     */
    public Tender acceptOffer(String tenderId, Offer offer) throws Exception {
        if (!offer.getStatus().equals(Status.ACCEPTED))
            throw new Exception();
        Tender tender = getTender(tenderId).orElse(null);
        if (tender != null && tender.getOffers().stream().noneMatch(o -> o.getStatus().equals(Status.ACCEPTED)) &&
                tender.getOffers().stream().anyMatch(o -> offer.getId().equals(o.getId()))) {
            tender.getOffers().forEach(
                    o -> o.setStatus(o.getId().equals(offer.getId()) ? Status.ACCEPTED : Status.REJECTED));
            tender = createTender(tender);
        } else {
            throw new Exception();
        }
        return tender;
    }

    /**
     * Gets offers for tender.
     *
     * @param tenderId the tender id
     * @return offers for tender
     * @throws Exception the exception
     */
    public List<Offer> getOffersForTender(String tenderId) throws Exception {
        Optional<Tender> optional = tenderRepository.findById(tenderId);
        Tender tender = getTender(tenderId).orElse(null);
        if (tender == null) {
            throw new Exception();
        }
        return tender.getOffers();
    }

    /**
     * Gets tenders by issuer.
     *
     * @param issuer the issuer
     * @return tenders by issuer
     */
    public List<Tender> getTendersByIssuer(String issuer) {
        List<Tender> tenders = tenderRepository.findTendersByIssuer(issuer);
        return tenders;
    }

    /**
     * Gets offers by bidder.
     *
     * @param bidder   the bidder
     * @param tenderId the tender id
     * @return offers by bidder
     */
    public List<Offer> getOffersByBidder(String bidder, String tenderId) {
        List<Tender> tenders;
        if (tenderId == null) {
            tenders = tenderRepository.findTendersByIdAndPath(bidder);
        } else {
            tenders = tenderRepository.findTendersByIdAndPath(bidder, tenderId);
        }
        List<Offer> offers = new LinkedList<>();
        tenders.forEach(t -> offers.addAll(t.getOffers()));
        return offers;
    }

}
