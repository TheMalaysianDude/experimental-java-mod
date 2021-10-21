package experimental;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import experimental.content.*;

public class ExperimentalJavaMod extends Mod{
	
	private final ContentList[] experimentalContent = {
		new ExperimentalBlocks()
	};
	/*
	public ExampleJavaMod(){
		
	}
	*/
	
	@Override
    public void loadContent(){
        for(ContentList list : experimentalContent){
            list.load();
        }
    }
}