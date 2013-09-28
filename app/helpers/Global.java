package helpers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {
	@Override
	  public void onStart(Application app) {
	    Logger.info("Application has started");
	    Executor e = Executors.newSingleThreadExecutor();
	    e.execute(new Poll());
	  } 
}
