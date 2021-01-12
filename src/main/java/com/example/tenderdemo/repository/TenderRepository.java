package com.example.tenderdemo.repository;

import com.example.tenderdemo.model.Tender;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * DAO Layer that performs operations to DB
 */
public interface TenderRepository extends MongoRepository<Tender, String> {

    /**
     * Find tenders by issuer list.
     *
     * @param issuer the issuer
     * @return the list
     */
    List<Tender> findTendersByIssuer(String issuer);

    /**
     * Find tenders by id and path list.
     *
     * @param bidder the bidder
     * @param id     the id
     * @return the list
     */
    @Query(value = "{'offers':{$elemMatch:{'bidder':?0}},'_id':ObjectId(?1)}")
    List<Tender> findTendersByIdAndPath(String bidder, String id);

    /**
     * Find tenders by id and path list.
     *
     * @param bidder the bidder
     * @return the list
     */
    @Query(value = "{'offers':{$elemMatch:{'bidder':?0}}}")
    List<Tender> findTendersByIdAndPath(String bidder);

}
