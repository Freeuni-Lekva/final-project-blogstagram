package org.blogstagram.followSystem.api;

import org.json.JSONObject;

public class ResponseJson {
    public static JSONObject initResponseJson(){
        JSONObject response = new JSONObject();
        JSONObject statusCodes = new JSONObject(StatusCodes.getMappings()) ;
        response.append("statusCodes", statusCodes);
        return response;
    }
}
