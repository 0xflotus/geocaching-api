package geocaching.api;

import geocaching.api.data.CacheLog;
import geocaching.api.data.FieldNote;
import geocaching.api.data.Geocache;
import geocaching.api.data.ImageData;
import geocaching.api.data.SimpleGeocache;
import geocaching.api.data.TravelBug;
import geocaching.api.data.UserProfile;
import geocaching.api.data.type.CacheType;
import geocaching.api.data.type.LogType;
import geocaching.api.exception.GeocachingApiException;
import geocaching.api.impl.live_geocaching_api.filter.CacheCodeFilter;
import geocaching.api.impl.live_geocaching_api.filter.Filter;
import geocaching.api.impl.live_geocaching_api.filter.GeocacheTypeFilter;
import geocaching.api.impl.live_geocaching_api.filter.PointRadiusFilter;

import java.util.Date;
import java.util.List;

public abstract class GeocachingApi {
  protected String session;
  
  public String getSession() {
      return session;
  }
  
  public void openSession(String session) throws GeocachingApiException {
      this.session = session;
  }
  
  public abstract void openSession(String userName, String password) throws GeocachingApiException;   
  public abstract void closeSession();
  public abstract boolean isSessionValid();
  
  public List<SimpleGeocache> getCachesByCoordinates(double latitude, double longitude, int startPosition, int endPosition, float radiusMiles) throws GeocachingApiException {
      return getCachesByCoordinates(latitude, longitude, startPosition, endPosition, radiusMiles, null);
  }
  
  public abstract TravelBug getTravelBug(String travelBugCode) throws GeocachingApiException;
  public abstract List<TravelBug> getTravelBugsByCache(String cacheCode) throws GeocachingApiException;
  public abstract List<CacheLog> getCacheLogs(String cacheCode, int startPosition, int endPosition) throws GeocachingApiException;
  
	@Deprecated
	public List<SimpleGeocache> getCachesByCoordinates(double latitude, double longitude, int startPosition, int endPosition, float radiusMiles,
			CacheType[] cacheTypes) throws GeocachingApiException {
		return searchForGeocaches(true, startPosition, endPosition - startPosition + 1, 5, -1, new Filter[] { 
				new PointRadiusFilter(latitude, longitude, (long) (radiusMiles * 1609L)),
				new GeocacheTypeFilter(cacheTypes)
		});
	}

	public SimpleGeocache getCacheSimple(String cacheCode) throws GeocachingApiException {
		List<SimpleGeocache> caches = getCaches(new String[] {cacheCode}, true, 0, 1, 0, 0);
		if (caches.size() == 0)
			return null;
		return caches.get(0);
	}

	public Geocache getCache(String cacheCode) throws GeocachingApiException {
		List<SimpleGeocache> caches = getCaches(new String[] {cacheCode}, false, 0, 1, 5, -1);
		if (caches.size() == 0)
			return null;
		return (Geocache) caches.get(0);
	}
	
	public List<SimpleGeocache> getCaches(String[] cacheCodes, boolean isLite, int startIndex, int maxPerPage, int geocacheLogCount, int trackableLogCount) throws GeocachingApiException {
		return searchForGeocaches(isLite, startIndex, maxPerPage, geocacheLogCount, trackableLogCount, new Filter[] {new CacheCodeFilter(cacheCodes)});
	}
	
	public abstract List<SimpleGeocache> searchForGeocaches(boolean isLite, int startIndex, int maxPerPage, int geocacheLogCount, int trackableLogCount, Filter[] filters) throws GeocachingApiException;
	
	public abstract UserProfile getYourUserProfile(boolean favoritePointData, boolean geocacheData, boolean publicProfileData, boolean souvenirData, boolean trackableData) throws GeocachingApiException;
	public abstract CacheLog createFieldNoteAndPublish(String cacheCode, LogType logType, Date dateLogged, String note, boolean publish, ImageData imageData, boolean favoriteThisCache) throws GeocachingApiException;
	
	public CacheLog createFieldNoteAndPublish(FieldNote fieldNote, boolean publish, ImageData imageData, boolean favoriteThisCache) throws GeocachingApiException {
		return createFieldNoteAndPublish(fieldNote.getCacheCode(), fieldNote.getLogType(), fieldNote.getDateLogged(), fieldNote.getNote(), publish, imageData, favoriteThisCache);
	}
}
