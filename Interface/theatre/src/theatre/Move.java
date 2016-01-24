package theatre;

public class Move implements Runnable {
	 
public void run(){
	//int attente = 10;
	while(true){
		 double start = System.nanoTime();
		 Scene.ourscene.updatePosition();
		 Scene.ourscene.repaint();
		 //txtpanel.ourtxtp.updatePosition();
		 //txtpanel.ourtxtp.repaint();
		 double timepassed = System.nanoTime() - start;
		 
		 //int wait = (int)(attente - timepassed/1000000);
		 //System.out.println(wait);
		 try{
			 Thread.sleep(100);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 }
}
	 
}
