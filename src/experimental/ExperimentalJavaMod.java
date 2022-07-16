package experimental;

import arc.*;
import arc.func.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;
import experimental.content.*;

public class ExperimentalJavaMod extends Mod{
	
	public ExperimentalJavaMod(){
		super();
	}
	
	@Override
	public void loadContent(){
		ExperimentalBlocks.load()
	}
	
	/*
	
	
	
	
	@Override
    public void loadContent(){
        for(ContentList list : experimentalContent){
            list.load();
        }
    }
	*/
}