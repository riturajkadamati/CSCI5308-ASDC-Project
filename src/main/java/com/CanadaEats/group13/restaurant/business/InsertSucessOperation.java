package com.CanadaEats.group13.restaurant.business;

import java.util.HashMap;
import java.util.Map;

import com.CanadaEats.group13.utils.StatePatternConstants;

public class InsertSucessOperation implements IRestaurantState {

    @Override
    public Map<String, String> setMessage() {
        HashMap<String, String> map = new HashMap<>();

        map.put(StatePatternConstants.getSuccessMessage(), StatePatternConstants.getInsertSuccess());
        return map;
    }

}
