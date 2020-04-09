package online.customAPIs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import backends.Functions;
import online.Requests;

public class GameJoltAPI extends Functions{

	private static final long serialVersionUID = -6405670062146508115L;
	
	private Requests req;
	
	private final String GJAPI = "https://api.gamejolt.com/api/game/";
	private final String VERSION = "v1_2";
	private final String GAME_ID;
	private final String GAME_KEY;
	private final String USER;
	private final String TOKEN;
	private final boolean AUTH;
	
	private String currentEndpoint = "";
	
	public GameJoltAPI(String game_id, String game_key, String username, String token) {
		
		req = new Requests();
		
		GAME_ID = game_id;
		GAME_KEY = game_key;
		USER = username;
		TOKEN = token;
		
		AUTH = Boolean.parseBoolean(auth().get("success").replace("\"", "").trim());
		
		if(!AUTH){
			System.err.println("Couldn't auth the current user...");
		}
	}
	
	private String encode(String input) {
		try {
			return URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return input;
		}
	}
	
	private String getBaseURL() {
		return GJAPI+VERSION+"/";
	}
	
	private String getSig(String completeURL) {
		return md5(completeURL);
	}
	
	private Map<String, String> splitKeys(String input){
		Map<String, String> ret = new HashMap<>();
		String[] split = input.split(System.lineSeparator());
		for(String s : split) {
			String[] temp = s.split(":");
			ret.put(temp[0], temp[1]);
		}
		return ret;
	}
	
	private Map<String, String> makeCall(String params) {
		String basic = getBaseURL()+currentEndpoint+"/?format=keypair&"+params;
		
		return splitKeys(req.get(basic+"&signature="+getSig(basic+GAME_KEY)));
	}
	
	private Map<String, String> sendBasics(){
		return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN);
	}
	
	public boolean getAuth() {
		return AUTH;
	}
	
	public Map<String, String> auth() {
		currentEndpoint = "users/auth";
		return sendBasics();
	}
	
	public Map<String, String> getUserData() {
		if(AUTH) {
			currentEndpoint = "users";
			return sendBasics();
		}
		return null;
	}
	
	public Map<String, String> open() {
		if(AUTH) {
			if(!Boolean.parseBoolean(check().get("success").replace("\"", "").trim())) {
				currentEndpoint = "sessions/open";
				return sendBasics();
			}
		}
		return null;
	}
	
	public Map<String, String> ping(){
		if(AUTH) {
			currentEndpoint = "sessions/ping";
			return sendBasics();
		}
		return null;
	}
	
	public Map<String, String> check() {
		if(AUTH) {
			currentEndpoint = "sessions/check";
			return sendBasics();
		}
		return null;
	}
	
	public Map<String, String> close() {
		if(AUTH) {
			if(Boolean.parseBoolean(check().get("success").replace("\"", "").trim())) {
				currentEndpoint = "sessions/close";
				return sendBasics();
			}
		}
		return null;
	}
	
	public Map<String, String> getScoreboards(int limit, boolean singleUser){
		if(AUTH) {
			String temp = "";
			if(limit < 0) {
				temp = "&limit="+limit;
			}
			if(singleUser) {
				temp = temp + "&username="+USER+"&user_token="+TOKEN;
			}
			currentEndpoint = "scores";
			return makeCall("game_id="+GAME_ID+temp);
		}
		return null;
	}
	
	public Map<String, String> getTables(){
		if(AUTH) {
			currentEndpoint = "scores/tables";
			return makeCall("game_id="+GAME_ID);
		}
		return null;
	}
	
	public Map<String, String> addScore(int tableID, int score, String denomination, String extras){
		if(AUTH) {
			currentEndpoint = "scores/add";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&score="+encode(score+" "+denomination)+"&sort="+score+"&extra_data="+extras+"&table_id="+tableID);
		}
		return null;
	}
	
	public Map<String, String> addScore(int tableID, int score, String denomination){
		return addScore(tableID, score, denomination, "null");
	}
	
	public Map<String, String> addScoreToDefault(int score, String denomination, String extras){
		if(AUTH) {
			currentEndpoint = "scores/add";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&score="+encode(score+" "+denomination)+"&sort="+score+"&extra_data="+extras);
		}
		return null;
	}
	
	public Map<String, String> addScoreToDefault(int score, String denomination){
		return addScoreToDefault(score, denomination, "null");
	}
	
	public Map<String, String> getRank(int tableID, int rank){
		if(AUTH) {
			currentEndpoint = "scores/get-rank";
			return makeCall("game_id="+GAME_ID+"&sort="+rank+"&table_id="+tableID);
		}
		return null;
	}
	
	public Map<String, String> getRankFromDefault(int rank){
		if(AUTH) {
			currentEndpoint = "scores/get-rank";
			return makeCall("game_id="+GAME_ID+"&sort="+rank);
		}
		return null;
	}
	
	public Map<String, String> getAllTrophies(){
		if(AUTH) {
			currentEndpoint = "trophies";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN);
		}
		return null;
	}
	
	public Map<String, String> getTrophies(boolean achieved){
		if(AUTH) {
			currentEndpoint = "trophies";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&achieved="+achieved);
		}
		return null;
	}
	
	public Map<String, String> getTrophy(String IDs){
		if(AUTH) {
			currentEndpoint = "trophies";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&trophy_id="+IDs);
		}
		return null;
	}
	
	public Map<String, String> achieveTrophy(String singleID){
		if(AUTH) {
			currentEndpoint = "trophies/add-achieved";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&trophy_id="+singleID);
		}
		return null;
	}
	
	public Map<String, String> unachieveTrophy(String singleID){
		if(AUTH) {
			currentEndpoint = "trophies/remove-achieved";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&trophy_id="+singleID);
		}
		return null;
	}
	
	public Map<String, String> setData(String key, String data){
		if(AUTH) {
			currentEndpoint = "data-store/set";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&key="+key+"&data="+data);
		}
		return null;
	}
	
	public Map<String, String> setGlobalData(String key, String data){
		if(AUTH) {
			currentEndpoint = "data-store/set";
			return makeCall("game_id="+GAME_ID+"&key="+key+"&data="+data);
		}
		return null;
	}
	
	public Map<String, String> removeData(String key){
		if(AUTH) {
			currentEndpoint = "data-store/remove";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&key="+key);
		}
		return null;
	}
	
	public Map<String, String> removeGlobalData(String key){
		if(AUTH) {
			currentEndpoint = "data-store/remove";
			return makeCall("game_id="+GAME_ID+"&key="+key);
		}
		return null;
	}
	
	public Map<String, String> getData(String key){
		if(AUTH) {
			currentEndpoint = "data-store/set";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&key="+key);
		}
		return null;
	}
	
	public Map<String, String> getGlobalData(String key){
		if(AUTH) {
			currentEndpoint = "data-store/set";
			return makeCall("game_id="+GAME_ID+"&key="+key);
		}
		return null;
	}
	
	public Map<String, String> getALLKeys(){
		if(AUTH) {
			currentEndpoint = "data-store/get-keys";
			return makeCall("game_id="+GAME_ID);
		}
		return null;
	}
	
	public Map<String, String> getPatternedKeys(String pattern){
		if(AUTH) {
			currentEndpoint = "data-store/get-keys";
			return makeCall("game_id="+GAME_ID+"&pattern="+pattern);
		}
		return null;
	}
	
	public Map<String, String> getUsersKeys(){
		if(AUTH) {
			currentEndpoint = "data-store/get-keys";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN);
		}
		return null;
	}
	
	public Map<String, String> getPatternedUserKeys(String pattern){
		if(AUTH) {
			currentEndpoint = "data-store/get-keys";
			return makeCall("game_id="+GAME_ID+"&username="+USER+"&user_token="+TOKEN+"&pattern="+pattern);
		}
		return null;
	}

}
