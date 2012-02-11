package geocaching.api.impl.live_geocaching_api.filter;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

public interface Filter {
	public abstract void writeJson(JsonWriter w) throws IOException;
	public abstract String getName();
	public abstract boolean isValid();
}
