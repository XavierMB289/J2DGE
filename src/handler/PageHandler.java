package handler;

import java.util.HashMap;
import java.util.Map;

import backends.objs.AppPage;
import backends.objs.Overlay;
import objs.progress.ProgressionCheck;

public class PageHandler {
	
	private Map<String, AppPage> pages = new HashMap<>();
	private String currentPage = "start";
	private Map<String, Overlay> overlays = new HashMap<>();
	private String currentOverlay = "";
	
	public ProgressionCheck pageCheck;

	public PageHandler() {
		pageCheck = new ProgressionCheck();
		
		pages = new HashMap<>();
		overlays = new HashMap<>();
	}
	
	public void setCurrentPage(String pageId, boolean init) {
		if(pages.get(pageId) != null) {
			//Only if there is a new page to go to, make sure to "exit" old page
			if(init) {
				if(getCurrentAppPage() != null) {
					getCurrentAppPage().onChange();
				}
				AppPage temp = pages.get(pageId);
				temp.init();
			}
			currentPage = pageId;
		} else {
			System.err.println("The page "+currentPage+" was not properly loaded in engine.Window");
			System.exit(-1);
		}
	}
	
	public void setCurrentOverlay(String id) {
		currentOverlay = id;
		if(overlays.get(currentOverlay) != null) {
			overlays.get(currentOverlay).init();
		}
	}
	
	public void setAllToCurrent(String id, boolean init) {
		setCurrentPage(id, init);
		setCurrentOverlay(id);
	}
	
	public void addPage(AppPage p) {
		if(!pages.containsValue(p)) {
			pages.put(p.getID(), p);
		}
	}
	
	public void addOverlay(Overlay o) {
		if(!overlays.containsValue(o)) {
			overlays.put(o.getID(), o);
		}
	}
	
	public String getPageName() {
		return currentPage;
	}
	
	public String getOverlayName() {
		return currentOverlay;
	}
	
	public AppPage getCurrentAppPage() {
		return pages.get(currentPage);
	}
	
	public Overlay getCurrentOverlay() {
		return overlays.get(currentOverlay);
	}
	
	public AppPage getLoadedPage(String page) {
		return pages.containsKey(page) ? pages.get(page) : null;
	}
	
	public Overlay getLoadedOverlay(String over) {
		return overlays.containsKey(over) ? overlays.get(over) : null;
	}
	
	public AppPage getCurrentLoadedPage() {
		return pages.containsKey(currentPage) ? pages.get(currentPage) : null;
	}
	
	public Overlay getCurrentLoadedOverlay() {
		return overlays.containsKey(currentOverlay) ? overlays.get(currentOverlay) : null;
	}
	
	public boolean compareAppPage(String comp) {
		return currentPage.equals(comp);
	}
	
	public boolean compareOverlay(String comp) {
		return currentOverlay.equals(comp);
	}
	
	public boolean containsAppPage(AppPage a) {
		return pages.containsValue(a);
	}
	
	public boolean containsOverlay(Overlay o) {
		return overlays.containsValue(o);
	}

}
