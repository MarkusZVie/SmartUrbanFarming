package ruleManagement;

public class PersistenceRBSThread extends Thread{

	private boolean runs;
	
	
	public synchronized void setRuns(boolean runs) {
		this.runs = runs;
	}


	public PersistenceRBSThread() {
		runs = true;
	}


	public void run() {
		System.out.println("PersistenceRBSThread Started");
		while(runs) {
			try {
				this.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("PersistenceRBSThread -> Fire Rules");
			System.out.println("PersistenceRBSThread -> facts:");
			for(String s: RuleManager.getInstance().getFacts()) {
				System.out.println(s);
			}
			RuleManager.getInstance().fireRules();
		}
	}

}
