package com.example.tenderdemo.controller;


import com.example.tenderdemo.model.Offer;
import com.example.tenderdemo.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller class that handles the requests
 */
@RestController
@RequestMapping("/v1/offers")
public class OfferController {

    @Autowired
    private TenderService tenderService;


    /**
     * Gets offers by bidder.
     *
     * @param bidder   the bidder
     * @param tenderId the tender id
     * @return the offers by bidder
     */
    @GetMapping()
    public ResponseEntity getOffersByBidder(@RequestParam String bidder, @RequestParam(required = false) String tenderId) {
        List<Offer> offers = tenderService.getOffersByBidder(bidder, tenderId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

}
