package datalinklayer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

class CacheArea {
	public Queue<buffer> isSendding;
	public buffer theNextACK;
	public Queue<buffer> nextSend;
	public PriorityQueue<buffer> received;
	public buffer recvbuf;
	public buffer recvLock;
	public CacheArea() {
		isSendding = new LinkedList<buffer>();
		nextSend = new LinkedList<buffer>();
		received = new PriorityQueue<>(new Comparator<buffer>(){
			 
	        @Override
	        public int compare(buffer c1, buffer c2) {
	            return (int) (c1.index - c2.index);
	        }
		});
		recvbuf =new buffer();
		recvLock = new buffer();
	}
}

class buffer{
	public int index=0;
	public int length=0;
	public boolean isACK=false;
	public long timestrap=0;
	public byte[] content;
}