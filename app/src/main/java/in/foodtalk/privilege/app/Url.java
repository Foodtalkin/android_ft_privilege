package in.foodtalk.privilege.app;

/**
 * Created by RetailAdmin on 02-05-2017.
 */

public class Url {
    private static String baseUrl = "http://stg-api.foodtalk.in/";

    public static String OFFERS = baseUrl+"offers";
    public static String OUTLET_OFFER = baseUrl+"outletoffer";
    public static String OFFER_OUTLET = baseUrl+"offer/outlet";

    public static String RESTAURANT = baseUrl+"restaurant";
    public static String RESTAURANT_OUTLETS = baseUrl+"restaurant/outlets";
    public static String OUTLET = baseUrl+"outlet";

    public static String SEARCH_TEXT = baseUrl+"search";
    public static String OFFER_DETAILS = baseUrl;

    public static String GET_OTP = baseUrl+"getotp";

    public static String USER_LOGIN = baseUrl+"userlogin";

    public static String CHECK_USER = baseUrl+"checkuser";
    public static String BOOKMARK = baseUrl+"bookmark";

    public static String REDEEM_HISTORY = baseUrl+"redeemhistory";

    public static String GET_CUISINE = baseUrl+"/cuisine";

    //redeemhistory?sessionid=4d17c8ecb555eeb99eb8849c6b58b66c93864a7d



    //http://stg-api.foodtalk.in/offers?city_zone_id=3&cuisine=2,1&cost=budget

    //http://stg-api.foodtalk.in/bookmark?sessionid=30f28e8eb0b90533ab8453b20d39506ce6088780
}
