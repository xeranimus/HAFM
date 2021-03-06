import java.util.ArrayList;

public class Profile {
    private ArrayList<Account> accountList;
    private ArrayList<Category> categoryList;
    private ArrayList<Transaction> transactionList;
    boolean devMode;

    private Profile(boolean devMode) {
        accountList = new ArrayList<Account>();
        categoryList = new ArrayList<Category>();
        transactionList = new ArrayList<Transaction>();
        this.devMode = devMode;
    }

    public Profile() {
        this(false);
    }

    //old adders based with data fields as arguments... maybe useful, but consider removing
    public void addAccount(String name, double balance) {
        accountList.add(new Account(name, balance));
        dprint("Adding account:\n");
        dprint(accountList.get(accountList.size()-1) + "\n");
    }

    public boolean addCategory(String name, double amount, Account account) {
        if (account == null) {
           dprint("ERROR. ACCOUNT == NULL");
            return false;
        }
        categoryList.add(new Category(name, amount, account));
        dprint("Adding category:\n");
        dprint(categoryList.get(categoryList.size()-1) + "\n");
        return true;
    }

    //make sure that Category is in the categoryList!!
    public void addTransaction(String name, double amount, String date, Category category, String notes) {
        //handle Category == null, or Account == null
        Transaction newTransaction = new Transaction(name, amount, date, category, notes);
        dprint("Adding transaction:\n");
        dprint(newTransaction + "\n");
        newTransaction.apply();
        transactionList.add(newTransaction);
    }

    public void addAccount(Account newAccount) {
        accountList.add(newAccount);
        dprint("Adding account:\n");
        dprint(accountList.get(accountList.size()-1) + "\n");
    }

    public boolean addCategory(Category newCategory) {
        if (newCategory.getAccount() == null) {
           dprint("ERROR. ACCOUNT == NULL");
            return false;
        }
        categoryList.add(newCategory);
        dprint("Adding category:\n");
        dprint(categoryList.get(categoryList.size()-1) + "\n");
        return true;
    }

    public void addTransaction(Transaction newTransaction) {
        //handle Category == null, or Account == null
        dprint("Adding transaction:\n");
        dprint(newTransaction + "\n");
        newTransaction.apply();
        transactionList.add(newTransaction);
    }

    //delete this if it doesn't work...
    public boolean removeAccount(Account toDelete) {
        if (!accountList.contains(toDelete)) {
            return false;
        }
        return removeAccount(accountList.indexOf(toDelete));
    }

    public boolean removeAccount(int index) {
        dprint("Removing account:\n");
        dprint(accountList.get(index) + "\n");
        boolean success = true;
        for (Category category : categoryList) {
            if (category.getAccount().equals(accountList.get(index))) {
                System.out.println("ERROR: Category still connected to account:\n" + category + "\n");
                success = false;
            }
        }
        if (success) {
            dprint("Success.\n");
            accountList.remove(index);
        }
        return success;
    }

    public boolean removeCategory(Category toDelete) {
        if (!categoryList.contains(toDelete)) {
            return false;
        }
        return removeCategory(categoryList.indexOf(toDelete));
    }

    public boolean removeCategory(int index) {
        dprint("Removing category:\n");
        dprint(categoryList.get(index) + "\n");
        boolean success = true;
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equals(categoryList.get(index))) {
                System.out.println("ERROR: Transaction still connected to category:\n" + transaction + "\n");
                success = false;
            }
        }
        if (success) {
            dprint("Success.\n");
            categoryList.remove(index);
        }
        return success;
    }

    public boolean removeTransaction(Transaction toDelete) {
        if (transactionList.contains(toDelete)) {
            return false;
        }
        return removeTransaction(transactionList.indexOf(toDelete));
    }
    //possible make this void
    public boolean removeTransaction(int index) {
        dprint("Removing transaction:\n");
        dprint(transactionList.get(index) + "\n");
        transactionList.get(index).unapply();
        return true;
    }

    public boolean updateCategory(Category oldCategory, Category newCategory) {
        if (!categoryList.contains(oldCategory)) {
            return false;
        }
        updateCategory(categoryList.indexOf(oldCategory), newCategory);
        return true;
    }

    public void updateCategory(int index, Category newCategory) {
        //resetting the remaining amount, so the transactions don't count twice
        newCategory.setRemaining(newCategory.getAmount());
        dprint("Updating category from:\n");
        dprint(categoryList.get(index) + "\n");
        dprint("to:\n" + newCategory + "\n");
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equals(categoryList.get(index))) {
                transaction.setCategory(newCategory);
            }
        }
        categoryList.remove(index);
        categoryList.add(index, newCategory);
    }

    public void updateTransaction(Transaction newTransaction) {
        return;
    }


    public Account getAccount(int index) {
        if (index < 0 || index >= accountList.size()) {
            return null;
        }
        return accountList.get(index);
    }

    public Account getAccount(String name) {
        for (Account account : accountList) {
            if (account.getName().equals(name)) {
                return account;
            }
        }
        return null;
    }

    public boolean isAccount(int index) {
        return (index > -1) && (index < accountList.size());
    }

    //is this method superfluous?
    public boolean isAccount(String name) {
        for (Account account : accountList) {
            if (account.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Category getCategory(int index) {
        if (index < 0 || index >= categoryList.size()) {
            return null;
        }
        return categoryList.get(index);
    }

    public Category getCategory(String name) {
        for (Category category : categoryList) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public boolean isCategory(int index) {
        return (index > -1) && (index < categoryList.size());
    }

    //is this method superfluous?
    public boolean isCategory(String name) {
        for (Category category : categoryList) {
            if (category.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Transaction getTransaction(int index) {
        if (index < 0 || index >= transactionList.size()) {
            return null;
        }
        return transactionList.get(index);
    }

    public Transaction getTransaction(String name) {
        for (Transaction transaction : transactionList) {
            if (transaction.getName().equals(name)) {
                return transaction;
            }
        }
        return null;
    }

    public boolean isTransaction(String name) {
        for (Transaction transaction : transactionList) {
            if (transaction.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    //may be unnecessary
    public boolean isTransaction(int index) {
        return (index > -1) && (index < transactionList.size());
    }

    public boolean hasAccount() {
        return (accountList.size() > 0);
    }

    public boolean hasCategory() {
        return (categoryList.size() > 0);
    }

    public boolean hasTransaction() {
        return (transactionList.size() > 0);
    }

    public String getAccountString() {
        int count = 0;
        String output = "";
		for (Account account : accountList) {
			count++;
			output += String.format("%-4s %-20s | $%-1s", "(" + count + ")", account.getName(), Menu.formatNum(account.getBalance()));
			output += "\n";
		}
		output += String.format("     %-20s | $%-1s", "Total", Menu.formatNum(getNetWorth()));
		output += "\n";
        return output;
    }

    public String getCategoryString() {
        int count = 0;
        double amountTotal = 0;
        double remainingTotal = 0;
        String output = "";
		for (Category category : categoryList) {
			count++;
            amountTotal += category.getAmount();
            remainingTotal += category.getRemaining();
            //holy moly, fix this line
			output += String.format("%-4s %-20s | $%-8s | $%-8s | $%-8s | %-1s", "(" + count + ")", category.getName(), Menu.formatNum(category.getAmount()), Menu.formatNum(category.getRemaining()), Menu.formatNum(category.getAmount() - category.getRemaining()), category.getAccount().getName());
			output += String.format("\n");
		}
		output += String.format("     %-20s | $%-8s | $%-8s | $%-8s", "Total" , Menu.formatNum(amountTotal), Menu.formatNum(remainingTotal), Menu.formatNum(amountTotal - remainingTotal));
		output += "\n";
        return output;
    }

    public String getTransactionString() {
        int count = 0;
        String output = "";
        for (Transaction transaction : transactionList) {
            count++;
            output += String.format("%-4s %-20s | $%-8s | %-7s | %-15s | %-1s" , "(" + count + ")", transaction.getName() , Menu.formatNum(transaction.getAmount()), transaction.getDate(), transaction.getCategory().getName(), transaction.getNotes());
            output += "\n";
        }
        return output;
    }

    public String getDelimited(String del) {
        String output = "";
        output += "Accounts\n";
        for (Account acc : accountList) {
            output += acc.getName() + del + Menu.formatNum(acc.getBalance()) + "\n";
        }
        output += "Categories\n";
        for (Category cat : categoryList) {
            output += cat.getName() + del;
            output += Menu.formatNum(cat.getAmount()) + del;
            output += Menu.formatNum(cat.getRemaining()) + del;
            output += Menu.formatNum(cat.getAmount() - cat.getRemaining()) + del;
            output += cat.getAccount().getName() + "\n";
        }
        output += "Transactions\n";
        for (Transaction tran : transactionList) {
            output += tran.getName() + del;
            output += Menu.formatNum(tran.getAmount()) + del;
            output += tran.getDate() + del;
            output += tran.getCategory().getName() + del;
            output += tran.getNotes() + "\n";
        }
        return output;
    }

    public String toString() {
        String output = "Accounts:\n" + getAccountString();
        output += "\nCategories:\n" + getCategoryString();
        output += "\nTransactions:\n" + getTransactionString();
        return output;
    }

    public double getNetWorth() {
		double total = 0;
		for (Account account : this.accountList)
				total += account.getBalance();
		return total;
	}

    void dprint(String input) {
        if (devMode) {
            System.out.print(input);
        }
    }

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        System.out.println("=====Welcome to the #Adulting Profile Manager v. 1.1=====\n");
        System.out.println("adding new Profile");
        Profile profile = new Profile(true);

        //creating accounts
        System.out.println("Creating new account:");
        System.out.println("Name: ");
        String name = input.nextLine();
        System.out.println("Amount: ");
        Double amount = Menu.getValidDouble(input);
        profile.addAccount(name, amount);
        System.out.println("Net Worth: " + profile.getNetWorth());
        System.out.println("Creating second new account:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        profile.addAccount(name, amount);
        System.out.println("Net Worth: " + profile.getNetWorth());

        //creating categories
        System.out.println("Creating new category:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Account: ");
        Account account = profile.getAccount(input.nextLine());
        while (account == null) {
            System.out.println("Error. Account not found.");
            account = profile.getAccount(input.nextLine());
        }
        profile.addCategory(name, amount, account);

        System.out.println("Creating second new category:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Account: ");
        account = profile.getAccount(input.nextLine());
        while (account == null) {
            System.out.println("Error. Account not found.");
            account = profile.getAccount(input.nextLine());
        }
        profile.addCategory(name, amount, account);

        //creating transactions
        System.out.println("Creating new transaction:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Date: ");
        String date = Menu.getValid(input, Menu.dateFormat);
        System.out.println("Category: ");
        Category category = profile.getCategory(input.nextLine());
        while (category == null) {
            System.out.println("Error. Category not found.");
            category = profile.getCategory(input.nextLine());
        }
        System.out.println("Notes: ");
        String notes = input.nextLine();
        profile.addTransaction(name, amount, date, category, notes);
        System.out.println("Net Worth: " + profile.getNetWorth());
        System.out.println("Creating new transaction:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Date: ");
        date = Menu.getValid(input, Menu.dateFormat);
        System.out.println("Category: ");
        category = profile.getCategory(input.nextLine());
        while (category == null) {
            System.out.println("Error. Category not found.");
            category = profile.getCategory(input.nextLine());
        }
        System.out.println("Notes: ");
        notes = input.nextLine();
        profile.addTransaction(name, amount, date, category, notes);
        System.out.println("Net Worth: " + profile.getNetWorth());

        System.out.println(profile.getAccountString());
        System.out.println(profile.getCategoryString());
        System.out.println(profile.getTransactionString());
        Data.save(profile, "test-delimited.xls", "\t");

        System.out.println("==========");
        System.out.println(profile);
        profile.removeAccount(0);
        System.out.println("Net Worth: " + profile.getNetWorth());
        profile.removeAccount(1);
        System.out.println("Net Worth: " + profile.getNetWorth());
        profile.removeCategory(0);
        profile.removeCategory(1);
        profile.removeTransaction(0);
        System.out.println("Net Worth: " + profile.getNetWorth());
        profile.removeTransaction(1);
        System.out.println("Net Worth: " + profile.getNetWorth());
    }
}
