package experimental;

import arc.*;
import arc.func.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;

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