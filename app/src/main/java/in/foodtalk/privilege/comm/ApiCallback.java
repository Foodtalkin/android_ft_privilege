package in.foodtalk.privilege.comm;

import org.json.JSONObject;

/**
 * Created by RetailAdmin on 02-05-2017.
 */

public interface ApiCallback {
    public void apiResponse(JSONObject response, String tag);
}
