package com.backend.database.adapters;

import com.backend.database.repositories.*;
import com.backend.jwt.user.UserUtil;
import com.backend.database.entities.*;
import com.backend.database.entities.keys.CharityVoteKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Adapter for interacting with charity data.
 * @author Ludvig Hygrell, JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
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

    /**
     * Find a charity based on it's orgId
     * @param orgId Identfier of the charity organization.
     * @return The resulting charity.
     * @throws Exception Thrown if getting the charity fails.
     */
    public Charity get(String orgId) throws Exception {
        return charityRepository.findById(orgId)
            .orElseThrow(() -> new Exception("Error getting charity."));
    }

    /**
     * Registers a vote from the user on the charity.
     * @param charity the charity that is being voted on.
     * @param value Whether the user voted up or down.
     * @return True if the vote was inserted.
     */
    public boolean vote(String charity, boolean value) {
        try {
            scoresRepository.save(new CharityVote(UserUtil.getUsername(), charity, value));        
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
            scoresRepository.save(new CharityVote(UserUtil.getUsername(), charity, value));
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
            scoresRepository.deleteById(new CharityVoteKey(UserUtil.getUsername(), charity));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Mark a charity as "paused" (only possible if UserUtil is admin).
     * @param charity_id Charity to pause.
     * @return True if successful.
     */
    public boolean pause(String charity_id) {
        try {
            Optional<Administrator> admin = administratorsRepository.findById(UserUtil.getUsername());
            if (admin.isEmpty() || admin.get().getLevel() < Administrator.PAUSE_CHARITY_LEVEL)
                return false;
            pausedCharitiesRepository.save(new PausedCharity(charity_id, UserUtil.getUsername()));            
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Resumes usage of a specified charity (only possible if UserUtil is admin).
     * @param charity_id Charity to resume use of.
     * @return True if successful.
     */
    public boolean resume(String charity_id) {
        try {
            // NOTE: There will most likely be a trigger for this eventually, however we are still checking just to be sure.
            Optional<Administrator> admin = administratorsRepository.findById(UserUtil.getUsername());
            if (admin.isEmpty() || admin.get().getLevel() < Administrator.PAUSE_CHARITY_LEVEL)
                return false;
            pausedCharitiesRepository.deleteById(charity_id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
