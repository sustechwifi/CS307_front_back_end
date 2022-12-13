package com.example.cs307front.interfaces;



public interface ICourier {
	boolean newItem(LogInfo log, ItemInfo item);

	boolean setItemState(LogInfo log, String name, ItemState s);
}
