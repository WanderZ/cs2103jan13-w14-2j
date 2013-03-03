package ezxpns.GUI; // This is a temporal storing location for compliation and testing. TO BE MOVED

import java.util.Date;

/**
 * This class contains the fields that are vital for retrieving records from the storage
 * Can probably be extended to facilitate the advanced search
 */
public class SearchRequest {
	
	private int type;
	private String name;
	private String category; // Maybe payment can be included
	private Date start, end;
	
	public SearchRequest() {}
	
}
