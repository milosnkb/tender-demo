package com.example.tenderdemo.model;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DTO Entity class
 */
public class Offer {

    @Id
    private String id;

    @NotNull(message = "Bidder is mandatory")
    private String bidder;

    private Status status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) &&
                Objects.equals(bidder, offer.bidder) &&
                status == offer.status;
    }

}
