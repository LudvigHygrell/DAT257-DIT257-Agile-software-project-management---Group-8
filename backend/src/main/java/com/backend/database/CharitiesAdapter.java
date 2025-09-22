package com.backend.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharitiesAdapter {

    @Autowired
    private CharityRepository charityRepository;

    @Autowired
    private CharityScoresRepository scoresRepository;

    @Autowired
    private PausedCharitiesRepository pausedCharitiesRepository;

    @Autowired
    private AdministratorsRepository administratorsRepository;

    protected CharitiesAdapter() {}

    public List<String> list(String[] filters, String order_by) {
        Pageable pageable = PageRequest.of(0,10); //TODO Implement sorting, and page parameter(?)
        Page<Charity> charitiesPage = charityRepository.findAll(pageable);
        return charitiesPage.getContent().stream().map(Charity::getOrgID).toList();
    }

    /**
     * Returns a charity given its organization ID
     * @param identity the charity that is to be returned
     * @return True if the charity was found
     */
    public boolean get(String identity) {return true;} //TODO what are we returning since the table only contains orgID?

    /**
     * Registers a vote from the user on the charity.
     * @param charity the charity that is being voted on.
     * @param value Whether the user voted up or down.
     * @return True if the vote was inserted.
     */
    public boolean vote(String charity, boolean value) {
        try{
            scoresRepository.save(new CharityVote(User.getCurrent().getUserName(), charity, value));        
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Change the value of a vote made by this user.
     * @param charity Charity that was voted on.
     * @param value New value of the vote.
     * @return True if successful
     */
    public boolean editVote(String charity, boolean value) {
        try {
            scoresRepository.save(new CharityVote(User.getCurrent().getUserName(), charity, value));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Remove a vote made by the current user.
     * @param charity Charity that was voted on. 
     * @return True if successful.
     */
    public boolean deleteVote(String charity) {
        try {
            scoresRepository.deleteById(new CharityVoteKey(User.getCurrent().getUserName(), charity));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Mark a charity as "paused" (only possible if User.getCurrent() is admin).
     * @param charity_id Charity to pause.
     * @return True if successful.
     */
    public boolean pause(String charity_id) {
        try {
            pausedCharitiesRepository.save(new PausedCharity(charity_id, User.getCurrent().getUserName()));            
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Resumes usage of a specified charity (only possible if User.getCurrent() is admin).
     * @param charity_id Charity to resume use of.
     * @return True if successful.
     */
    public boolean resume(String charity_id) {
        try {
            // NOTE: There will most likely be a trigger for this eventually, however we are still checking just to be sure.
            Optional<Administrator> admin = administratorsRepository.findById(User.getCurrent().getUserName());
            if (!admin.isPresent() || admin.get().getLevel() < Administrator.PAUSE_CHARITY_LEVEL)
                return false;
            pausedCharitiesRepository.deleteById(charity_id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
