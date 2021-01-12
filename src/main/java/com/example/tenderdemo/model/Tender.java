package com.example.tenderdemo.model;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * DTO Entity class
 */
public class Tender {

    private String id;

    @NotNull(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Issuer is mandatory")
    private String issuer;

    private List<Offer> offers;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Offer> getOffers() {
        if (offers == null)
            offers = new ArrayList<>();
        return offers;
    }
}
