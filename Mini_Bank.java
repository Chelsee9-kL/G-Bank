//import classes for input and output operations
import java.io.*;
//import the HashMap class used to store accounts
import java.util.HashMap;
//import the Map interface implemented by HashMap
import java.util.Map;
//import the Scanner class used to read input from user
import java.util.Scanner;
import java.util.UUID;
import java.util.Random;

//Define a new class Compte that implements serializable interface which allows objects of this class to be written into file 
class Compte implements Serializable {
    private String numeroCompte;
    private String nom;
    private double solde;

    //A constructor for the Compte class which initializes a new account with the given account number,name and balance
    public Compte(String numeroCompte, String nom, double solde) {
        this.numeroCompte = numeroCompte;
        this.nom = nom;
        this.solde = solde;
    }

//Getters method that allows access to the private fields of the Compte class
    public String getNumeroCompte() {
        return numeroCompte;
    }
    public String getNom() {
        return nom;
    }
    public double getSolde() {
        return solde;
    }
//This update the account balance by adding the given amount
    public void deposer(double montant) {
        solde += montant;
    }
    public  void retirer(double montant) {
        solde -= montant;
    }

}

//Define new class Mini_Bank with 3 private field
    public class Mini_Bank {
        private Map<String, Compte> comptes;
        private Scanner scanner;
        private String filename = "comptes.dat";
        private Random random = new Random();

        //Constructor which initializes a new bank with an empty map of account, a Scanner object and loads existing account from file
        public Mini_Bank() {
            comptes = new HashMap<>();
            scanner = new Scanner(System.in);
            loadComptes();
        }

    //THis loads existing accounts from the file into the comptes map
        private void loadComptes() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))){
                comptes = (Map<String, Compte>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                //handle exception
            }
        }

    //This saves the current state of the comptes map
        private void saveComptes() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(comptes);
            } catch (IOException e) {
                //handle exception
            }
        }
        private String generateNumeroCompte() {
            //String numeroCompte = UUID.randomUUID().toString();
            //System.out.print("NUmero de compte genere':" + numeroCompte);
            StringBuilder sb = new StringBuilder("Mini_Bank");
            for(int i = 0; i<8; i++) {
                sb.append(random.nextInt(10));
               // numeroCompte += random.nextInt(10);
           }
            return sb.toString();
        }

    //This displays the main menu of the bank and ask the user to choose an option
        public void menu() {
            System.out.println("1. creer compte");
            System.out.println("2. acceder `a un compte existant");
            System.out.println("3. Supprimer un compte");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option :");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour `a la ligne

            //The switch handles the user's choice
            switch (option) {
                case 1:
                    creerCompte();
                    break;
                case 2:
                    accederCompte();
                    break;
                case 3:
                    supprimerCompte();
                    break;
                case 4:
                    saveComptes();
                    System.out.println("You have exit !");
                    break;
                default:
                    System.out.println("Option invalide.");
                    System.out.print("Choisisez une option de 1-3.");
                    menu();
                }
            }
         //This creates a new account
             public void creerCompte() {
                String numeroCompte = generateNumeroCompte();
                System.out.println("Numero de compte:" + numeroCompte);
               // while(comptes.containsKey(numeroCompte)){
                    //numeroCompte = generateNumeroCompte();
                //}
               
                System.out.print("Entrez votre nom :");
                String nom = scanner.nextLine();
                System.out.print("Entrez `a nouveau votre nom pour valider : ");
                String nomValidation = scanner.nextLine();

                //This  checks if the user's name matches the confirmation name
                //if it match, it checks if the account number is already in use and if available, a new Compte onjecy is created and added to Comptes map
                //DeposerArgent method is called to deposit money into the new account and if in use an error message is generated
                //If user's name does not match the confirmation name ,an error is displayed and finally the menu() method is called to return to main menu
                if(nom.equals(nomValidation)) {
                    if (!comptes.containsKey(numeroCompte)) {
                        Compte compte = new Compte(numeroCompte, nom, 0.0);
                        comptes.put(numeroCompte, compte);
                        System.out.println("Compte cre'e avec succes.");
                        saveComptes();
                        deposerArgent(compte);
                    }else{
                        System.out.println("Numero de compte deja existant.");
                    }
                }else{
                    System.out.println("Nom the validtion invalide.");
                }
                menu();
             }
        //This allows usser to acces existing account
             public void accederCompte() {
                System.out.print("Entrez le numero de compte :");
                String numeroCompte = scanner.nextLine();

                //Ask user to enter name if account number is found  in comptes map
                if (comptes.containsKey(numeroCompte)){
                    Compte compte = comptes.get(numeroCompte);
                    System.out.print("Entrez votre nom :");
                    String nom = scanner.nextLine();
                

                if (compte.getNom().equals(nom)) {
                    menuCompte(compte);
                }else{
                    System.out.println("Nom de compte invalide.");
                }
             }else{
                System.out.println("Numero de compte invalide.");
             }
             menu();
        }
        //To delete an existing account
        public void supprimerCompte() {
         System.out.print("Entrez le numero du compte a suprimer:");
        String numeroCompte = scanner.nextLine();
        if(comptes.containsKey(numeroCompte)) {
            Compte compte = comptes.get(numeroCompte);

            System.out.println("Entrez le nom du compte pour confirmatoin:");
               String nom = scanner.nextLine();
               if(compte.getNom().equals(nom)) {
                System.out.print("Voulez vous vraiment le supprimer?(oui/non):");
                String confirmation = scanner.nextLine();
                if(confirmation.equalsIgnoreCase("oui")) {
                comptes.remove(numeroCompte);
                System.out.println("Compte supprimer avec succes.");
                saveComptes();
                }else{
                    System.out.println("Suppression annulee");
                }
               }else{
                System.out.println("Nom de compte invalide");
               }
            }else{
                System.out.println("Numero de compte invalide.");
            }
            menu();
        }
        //This displays the account menu and ask the user to choose an option
        public void menuCompte(Compte compte) {
            //while(true) ensures that the menu is displayed repeatedly until user chooses to return to the main menu 
            while(true) {
                System.out.println("1. Deposer de l'argent");
                System.out.println("2. Retirer de l'argent");
                System.out.println("3. Consulter le solde");
                System.out.println("4. Retour au menu principal");
                System.out.println("Choisissez une option :");
                int option = scanner.nextInt();
                scanner.nextLine(); //Consommer le retour `a la ligne

                //This switch handles the user's choice
                switch (option) {
                    case 1:
                        deposerArgent(compte);
                        break;
                    case 2:
                        retirerArgent(compte);
                        break;
                    case 3:
                        ConsulterSolde(compte);
                        break;
                    case 4:
                        menu ();
                        return;
                    default:
                        System.out.println("Option invalide.");
                }
            }
        }
        //This allows the user to deposit money into the account
        public void deposerArgent(Compte compte) {
            System.out.print("Entrez le montant `a de'poser");
            double montant = scanner.nextDouble();
            scanner.nextLine(); 

            //if montant>=25000 the deposerArgent method is called to update the account balance and a success message is displayed else an error message is displayed
            //SaveComptes() is calles to save the updated account balance
            if (montant >=25000) {
                compte.deposer(montant);
                System.out.println("De'pot re'ussi. Nouveau solde :" +compte.getSolde());
                saveComptes();
            }else{
                System.out.println("Montant invalide.");
                System.out.println("Svp entrez un montant>=25000");
            }
            menuCompte(compte);
        }
        public void retirerArgent(Compte compte) {
            System.out.print("Entrez le montant a retirer:");
            double montant = scanner.nextDouble();
            scanner.nextLine();

            if(montant >=25000 && montant <compte.getSolde() ){
                compte.retirer(montant);
            System.out.println("Retrait reussi.Nouveau solde:" +compte.getSolde());
            saveComptes();
            }else{
                System.out.println("Montant invalide ou solde insuffisant.");
            }
            menuCompte(compte);
        }
        //This displays the current account balance
        public void ConsulterSolde(Compte compte) {
            System.out.println("Solde actuel : " + compte.getSolde());
            System.out.println("Press enter to continue...");
            scanner.nextLine();
        }
        //This is the main method where the program starts execution
        //A new Mini_Bank is created and the menu method is called to display the main menu
        public static void main(String[] args) {
            Mini_Bank miniBank = new Mini_Bank();
            miniBank.menu();
        }
    }

