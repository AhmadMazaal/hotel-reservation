import menu.MainMenu;

// ------- IMPROVEMENTS  -------
//TODO: Seeders should only communicate with the API layer, so far it is not
//TODO: Complete Seeders functionality
//TODO: Document the project with the material learned
// ------- IMPROVEMENTS  -------

public class HotelDriver {
    public static void main(String []args){
        MainMenu menu = new MainMenu();
        menu.initMainMenu();
    }
}