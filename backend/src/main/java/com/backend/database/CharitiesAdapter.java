package com.backend.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharitiesAdapter {

    @Autowired
    private final CharityRepository charityRepository;

    public CharitiesAdapter(CharityRepository repo) {charityRepository = repo;}

    public boolean list(String[] filters, String order_by) {return true;}

    /**
     * Returns a charity given its organization ID
     * @param identity the charity that is to be returned
     * @return True if the charity was found
     */
    public boolean get(String identity) {return true;}

    /**
     * Registers a vote from the user on the charity
     * @param charity the charity that is being voted on
     * @param up the vote value. true = upvote, false = downvote
     * @return True if the vote was inserted
     */
    public boolean vote(String charity, boolean up) {
        try{
            //TODO
        } catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean edit_vote(String charity, boolean up) {return true;}
    public boolean delete_vote(String user, String charity) {return true;}
    public boolean pause(String charity_id) {return true;}
    public boolean resume(String charity_id) {return true;}
    public boolean get_paused(String[] filters, String order_by) {return true;}
}
