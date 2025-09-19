package com.backend.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharitiesAdapter {

    @Autowired
    private final CharityRepository charityRepository;

    @Autowired
    private final LikeRepository likeRepository;

    public CharitiesAdapter(CharityRepository charityRepository, LikeRepository likeRepository) {
        this.charityRepository = charityRepository;
        this.likeRepository = likeRepository;
    }

    public List<String> list(String[] filters, String order_by) {
        Pageable pageable = PageRequest.of(0,10); //TODO Implement sorting, and page parameter(?)
        Page<Charity> charitiesPage = charityRepository.findAll(pageable);
        return charitiesPage.getContent().stream().map(Charity::getOrgID).toList();
    }//TODO

    /**
     * Returns a charity given its organization ID
     * @param identity the charity that is to be returned
     * @return True if the charity was found
     */
    public boolean get(String identity) {return true;} //TODO what are we returning since the table only contains orgID?

    /**
     * Registers a vote from the user on the charity.
     * @param charity the charity that is being voted on.
     * @param up the vote value. true = upvote, false = down vote.
     * @return True if the vote was inserted.
     */
    public boolean vote(String charity, boolean up) { //TODO Refer to Issue #1
        try{
            //Identify if vote already exists from user
            //If not, place vote
        } catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean edit_vote(String charity, boolean up) {return true;}//TODO Refer to Issue #1

    /**
     * Remove a user's vote from a charity page.
     * @param user The user who placed the vote to be removed.
     * @param charity The charity the vote was placed on.
     * @return True if the vote was successfully removed.
     */
    public boolean delete_vote(String user, String charity) {
        try{
            likeRepository.deleteById(new LikeKey(user,charity));
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public boolean pause(String charity_id) {return true;}//TODO
    public boolean resume(String charity_id) {return true;}//TODO
    public boolean get_paused(String[] filters, String order_by) {return true;}//TODO
}
