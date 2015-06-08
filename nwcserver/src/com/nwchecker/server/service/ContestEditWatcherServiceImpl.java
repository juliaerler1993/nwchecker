package com.nwchecker.server.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "ContestEditWatcher")
public class ContestEditWatcherServiceImpl implements ContestEditWatcherService {
    private static Map<Integer, String> lastEditorInContest = new HashMap<>();
    private static Map<String, Integer> lastEditedContestByUser = new HashMap<>();

    private final static Set<String> EDIT_URL_SET = new HashSet<>();

    static {
        EDIT_URL_SET.add("/getContestUsersList.do");
        EDIT_URL_SET.add("/newTaskForm.do");
    }

    @Autowired
    private PageTrackingService pageTrackingService;

    @Override
    public void addParameters(int contestId, String username){
        lastEditorInContest.put(contestId, username);
        lastEditedContestByUser.put(username, contestId);
    }

    @Override
    public boolean checkContestIsEditedById(int contestId){
        String lastEditorUsername = lastEditorInContest.get(contestId);
        String lastUserURL = pageTrackingService.getPathByUsername(lastEditorUsername);

        return (EDIT_URL_SET.contains(lastUserURL)
                && lastEditedContestByUser.get(lastEditorUsername)!= null
                && lastEditedContestByUser.get(lastEditorUsername)==contestId)
                ? true : false;
    }

}
