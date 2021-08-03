package ethos.model.content.collection;

public enum CollectionLogType {
	KILL("killed"), CLUE("opened"), MINIGAMES("completed"), OTHER("completed");
	
	public String actionWord;
	
	CollectionLogType(String actionWord) {
		this.actionWord = actionWord;
	}
}