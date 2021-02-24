package com.mycompany.mavenproject1;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;


/** 
  *   AccountBalance defines the balance of the users at a given time
      in the ledger model of bitcoins
  */

public class AccountBalance {

    /** 
     * The current balance of each user, with each account's name mapped to its 
     *    current balance.
     */
    
    private TreeMap<String, Integer> accountBalanceBase;

    /** 
     * Constructor for creating empty AccountBalance
     */

    public AccountBalance() {
	accountBalanceBase = new TreeMap<String, Integer>();
    }

    /** 
     * Constructor for creating AccountBalance from a map from string to integers
     */
    
    public AccountBalance(TreeMap<String, Integer> accountBalanceBase) {
	this.accountBalanceBase = accountBalanceBase;
    }

    /** 
     * obtain the underlying Treemap from string to integers
     */   
    
    public TreeMap<String,Integer> getAccountBalanceBase(){
	return accountBalanceBase;
    };

    /** 
      * obtain the list of users in the tree map
      */   
    
    public Set<String> getUsers(){
	return getAccountBalanceBase().keySet();
    };    

    
    

    /** 
     * Adds an account for user with balance.
     *
     * if there was an entry it is overridden.  
     */

    public void addAccount(String user, int balance) {
	accountBalanceBase.put(user, balance);
    }

    /** 
     * @return true if the {@code user} exists in AccountBalance
     */
    
    public boolean hasUser(String user) {
	return accountBalanceBase.containsKey(user);
    }


    /** 
     * @return the balance for this account {@code account}
     *
     *  if there was no entry, return zero
     *
     */
    
    public int getBalance(String user) {
	if (hasUser(user)){
		return accountBalanceBase.get(user);
	    } else
	    {  return 0;
	    }
    }


    /** 
     * set the balance for {@code user} to {@code amount}
     *  this will override any existing entries
     */

    
    public void setBalance(String user, int amount){
	accountBalanceBase.put(user,amount);
	    };
	

    /** 
     *  Adds amount to balance for {@code user}
     * 
     *  if there was no entry for {@code user} add one with 
     *       {@code balance}
     */
    
    public void addBalance(String user, int amount) {
	setBalance(user,getBalance(user) + amount);
    }


    /** 
     *   Subtracts amount from balance for {@code user}
     */
    
    public void subtractBalance(String user, int amount) {
	setBalance(user,getBalance(user) - amount);
    }


    /** 
     * Check balance has at least amount for {@code user}
     */

    public boolean checkBalance(String user, int amount) {
	return (getBalance(user) >= amount);
    }    

    /** 
     *
     *  Task 1: Fill in the body of method checkAccountBalanceDeductable()
     *          It has been commented out so that the code compiles.
     *
     * Check all items in accountBalance2 can be deducted from the current one
     *
     *   accountBalance2 is usually obtained
     *   from a list of inputs of a transaction
     *
     * Checking that a TxOutputList  can be deducted will be later done
     *  by first converting that TxOutputList into an AccountBalance and then using
     *    this method
     *
     * A naive check would just check whether each entry of a TxOutputList can be 
     *   deducted
     *
     * But there could be an output for the same user Alice of say 10 units twice
     *   where there are not enough funds to deduct it twice but enough
     *   funds to deduct it once
     * The naive check would succeed, but after converting the TxOutputList
     *  to AccountBalance we have that for Alice 20 units have to be deducted
     *  so the deduction of the accountBalance created fails.
     *
     * One could try for checking that one should actually deduct each entry in squence
     *   but then one has to backtrack again.
     * Converting the TxOutputList into a AccountBalance is a better approach since the
     *   TxOutputList is usually much smaller than the main AccountBalance.
     * 
     */    

    
    public boolean checkAccountBalanceDeductable(AccountBalance accountBalance2){
	// fill in Body 
        boolean isDeductable = false;
        Set<String> users = accountBalance2.getUsers();
        for(String name : users){
            int amount = accountBalance2.getBalance(name);
            isDeductable = checkBalance(name, amount);
        }
        return isDeductable;
    }
    
    
    /** 
     *
     *  Task 2: Fill in the method checkTxELdeductable 
     *          It has been commented out so that the code compiles.It checks that a list of txEntries (which will be inputs of a transactions)
     can be deducted from AccountBalance

   done by first converting the list of txEntries into an accountBalance
     and then checking that the resulting accountBalance can be deducted.
     *
     *
     * @param txel   
     * @return    
     */    

        
    public boolean checkTxELdeductable(TxEntryList txel){
	// fill in Body 
        boolean isDeductable = false;
        AccountBalance acc_balance =  txel.toAccountBalance();
        isDeductable = checkAccountBalanceDeductable(acc_balance);
        return isDeductable;
    }
    
    
    /** 
     *  Task 3: Fill in the methods subtractTxEL and  addTxEL.
     *
     *   Subtract a list of txEntries (txel, usually transaction inputs) from the accountBalance 
     *
     *   requires that the list to be deducted is deductable.
     *   
     */    
    
    
    public void subtractTxEL(TxEntryList txel){
	// fill in Body 
        AccountBalance acc_balance = txel.toAccountBalance();
        Set<String> acc_users = acc_balance.getUsers();
        
        for(String name : acc_users){
            int amount = acc_balance.getBalance(name);
            
            if(hasUser(name)){
                subtractBalance(name, amount);
            }
        }
    }

    



    /** 
     * Add a list of txEntries (txel, usually transaction outputs) to the current accountBalance
     *
     */    

       public void addTxEL(TxEntryList txel){
	   // fill in Body 
           AccountBalance acc_balance = txel.toAccountBalance();
           Set<String> acc_users = acc_balance.getUsers();
           
           for(String name : acc_users){
               int amount = acc_balance.getBalance(name);

               if(!hasUser(name)){
                   addAccount(name, amount);
               }else{
                   addBalance(name, amount);
               }
           }
       }


    /** 
     *
     *  Task 4: Fill in the method checkTxValid
     *          It has been commented out so that the code compiles.
     *
     * Check a transaction is valid:
     *    the sum of outputs is less than the sum of inputs
     *    and the inputs can be deducted from the accountBalance.
     *
     */    

    
    public boolean checkTxValid(Tx tx){
	// fill in Body 
        boolean txValid = false;
        boolean isValid = tx.checkTxAmountsValid();
        if (isValid && checkTxELdeductable(tx.toInputs())){
            txValid = true;
        }
        
        return txValid;
    }
    

    /** 
     *
     *  Task 5: Fill in the method processTx
     *
     * Process a transaction
     *    by first deducting all the inputs
     *    and then adding all the outputs.
     *
     */    
    
    public void processTx(Tx tx){
	// fill in Body 
        if(checkTxValid(tx)){
            TxEntryList transInput = tx.toInputs();
            subtractTxEL(transInput);
            TxEntryList transOutput = tx.toOutputs();
            addTxEL(transOutput);
        }
    };
    

    /** 
     * Prints the current state of the accountBalance. 
     */
    
    public void print() {
	for (String user : accountBalanceBase.keySet()) {
	    Integer value = accountBalanceBase.get(user).intValue();
	    System.out.println("The balance for " + user + " is " + value); 
	}

    }


    /** 
     *  Task 6: Fill in the testcases as described in the labsheet
     *    
     * Testcase
     */
    public void testCase(String header){
        System.out.println();
        System.out.println(header);
    }
    
    public static void test() {
	// fill in Body 
        AccountBalance bl;
        (new AccountBalance()).testCase("Starting test");

        
        System.out.println();
        
        bl = new AccountBalance();
        
        bl.testCase("test case initial account");
        bl.addAccount("Alice", 0);
        bl.addAccount("Bob", 0);
        bl.addAccount("Carol", 0);
        bl.addAccount("David", 0);
        bl.print();
        
        bl.testCase("test case to set account");
        bl.setBalance("Alice", 20);
        bl.setBalance("Bob", 15);
        bl.print();
        
        bl.testCase("test case to subtract 5 from Bob");
        bl.subtractBalance("Bob", 5);
        bl.print();
        
        bl.testCase("test case to check txel1 is deductable");
        TxEntryList txel1 = new TxEntryList("Alice", 15, "Bob", 5);
        System.out.println(bl.checkTxELdeductable(txel1));
        bl.print();
        
        bl.testCase("test case to check txel2 is deductable");
        TxEntryList txel2 = new TxEntryList("Alice", 15, "Alice", 15);
        System.out.println(bl.checkTxELdeductable(txel2));
        bl.print();
        
        bl.testCase("test case to subtract txel1 from balance");
        bl.subtractTxEL(txel1);
        bl.print();
        
        bl.testCase("test case to add txel2 to balance");
        bl.addTxEL(txel2);
        bl.print();
        
        bl.testCase("test case to check if tx1 is valid");
        TxEntryList input1 = new TxEntryList("Alice", 40);
        TxEntryList output1 = new TxEntryList("Bob", 5, "Carol", 20);
        Tx tx1 = new Tx(input1, output1);
        System.out.println(bl.checkTxValid(tx1));
        bl.print();
        
        bl.testCase("test case to check if tx2 is valid");
        TxEntryList input2 = new TxEntryList("Alice", 20);
        TxEntryList output2 = new TxEntryList("Bob", 5, "Carol", 20);
        Tx tx2 = new Tx(input2, output2);
        System.out.println(bl.checkTxValid(tx2));
        bl.print();
        
        bl.testCase("test case to check if tx3 is valid");
        TxEntryList input3 = new TxEntryList("Alice", 25);
        TxEntryList output3 = new TxEntryList("Bob", 5, "Carol", 20);
        Tx tx3 = new Tx(input3, output3);
        System.out.println(bl.checkTxValid(tx3));
        bl.print();
        
        //update account balance
        bl.testCase("test case to process tx3");
        bl.processTx(tx3);
        bl.print();
        
        bl.testCase("test case to process tx4");
        TxEntryList input4 = new TxEntryList("Alice", 5, "Alice", 5);
        TxEntryList output4 = new TxEntryList("Bob", 10);
        Tx tx4 = new Tx(input4, output4);
        
        bl.processTx(tx4);
        bl.print();
    }
    
    /** 
     * main function running test cases
     */            

    public static void main(String[] args) {
	AccountBalance.test();	
    }
}
