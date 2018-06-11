package com.debasish.guitardhun.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.models.AccessoriesModel;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.debasish.guitardhun.utils.LoaderUtils;
import com.debasish.guitardhun.utils.RandomNumberGenerator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    ArrayList<String> colorArray = new ArrayList<>();
    ArrayList<String> typeArray = new ArrayList<>();
    ArrayList<String> priceArray = new ArrayList<>();
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> imageArray = new ArrayList<>();
    ArrayList<AccessoriesModel> accessoriesModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
       // getSupportActionBar().hide();

       // addAccessories();

        //Redirecting the user to the LoginScreen
        redirectUser();
    }

    /*Harley Benton Parts Locking Tuners 6L Gold, banez UTA20 Ultralite Tremolo Arm
     Behringer Pedal Board PB600 , Fender Stratocaster Accessory Kit Black
    Cherub GT-3 4-Band EQ Guitar Pickup and Preamp, Vox VAC19 Guitar Cable 6 metres
    Vox VAC19 Guitar Cable 6 metres, Hercules GSP29WB Stands Autogrip Universal Hanger
     Konig & Meyer Guitar Holder for Mic Stands/Black , Konig & Meyer 1757000000 Guitar Stand
    Seymour Duncan Quarter Pound Jazz Bass Pickup Set , ENO EHF-01 Hand and Finger Exerciser
    Vault EHF-01 Hand and Finger Exerciser , Ashton SHM5 Guitar Sound Hole Muffler
    Hercules Stands GSP38WB Wood Wallmount Guitar , HERCULES FS100B Guitar Foot Rest
    Vault ESC-01 String Cleaner , Levy's M8POLYBLK Guitar Strap , Hercules GSP40SB Acoustic Guitar Slatwall Hanger
    ENO EHF-01 Hand and Finger Exerciser - Open Box , Pluto AHF-03 Finger Exerciser , DiMarzio DP123BK Bass Guitar Pickup - Model J
    Gibson Gear PRHK-020 top hat knobs-Gold , Fender Light Grip Hand Exerciser
    Bajaao Electric Guitar Deluxe Accessory Bundle , Bajaao Electric Guitar Standard Accessory Bundle
    EMG HZ7-A Passive 7-String Humbucker Pickup , Guitar Facelift Stars n Stripes for Stratocaster
    Guitar Facelift Francis Rossi , Dunlop 6500 System 65 Guitar Maintenace Kit
    Ibanez GWS100 Powerpad Guitar Workstation, Gator G-Tour Head ATA Tour Case for Amp Head
    Fender Precision Bass Pickguard, 13 Hole, Truss Rod
    Fender Tex-Mex Strat Pickups, Set of 3 - White, Fender Custom Shop Custom '54 Stratocaster
    Ibanez IFT10 Guitar Finger Trainer , Ibanez PGS32AC Guitar Stand
    DiMarzio DP810BK Guitar Pickup - Ionizer 8 (Tosin), DiMarzio DP419W Guitar Pickup - Area '67 Strat Guitar
    DiMarzio DP416W -Area '61 Single Coil Stratocaster , Hercules BS118BB Music Stand With Bag
    Hercules GSP50HB WALL GRIDS Guitar Hanger , Hercules GS414X Home Series Guitar Floor Stand*/


   /* public void addAccessories() {
        nameArray.add("Harley Benton Parts Locking Tuners 6L Gold");
        nameArray.add("banez UTA20 Ultralite Tremolo Arm");
        nameArray.add("Behringer Pedal Board PB600");
        nameArray.add("Fender Stratocaster Accessory Kit Black");
        nameArray.add("Ibanez GWS100 Powerpad Guitar Workstation");
        nameArray.add("Hercules GS414X Home Series Guitar Floor Stand");
        nameArray.add("Fender Tex-Mex Strat Pickups");
        nameArray.add("Bajaao Electric Guitar Deluxe Accessory Bundle");
        nameArray.add("Vault EHF-01 Hand and Finger Exerciser ");
        nameArray.add("Fender Precision Bass Pickguard");
        nameArray.add("DiMarzio DP416W -Area '61 Single Coil Stratocaster");
        nameArray.add("Strap");
        nameArray.add("Hercules GSP50HB WALL GRIDS Guitar Hanger");
        nameArray.add("Ibanez GWS100 Powerpad Guitar Workstation");
        nameArray.add("Guitar Facelift Stars n Stripes for Stratocaster");
        nameArray.add("Set of 3 - White, Fender Custom Shop Custom '54 Stratocaster");
        nameArray.add("Ibanez PGS32AC Guitar Stand");
        nameArray.add("Pluto AHF-03 Finger Exerciser");
        nameArray.add("Hercules BS118BB Music Stand With Bag");
        nameArray.add("Hercules GS414X Home Series Guitar Floor Stand");

        priceArray.add("150");
        priceArray.add("600");
        priceArray.add("900");
        priceArray.add("200");
        priceArray.add("170");
        priceArray.add("100");
        priceArray.add("9000");
        priceArray.add("60000");
        priceArray.add("1700");
        priceArray.add("3000");
        priceArray.add("3300");
        priceArray.add("2600");
        priceArray.add("8900");
        priceArray.add("9000");
        priceArray.add("5700");
        priceArray.add("3700");
        priceArray.add("1400");
        priceArray.add("5000");
        priceArray.add("220");
        priceArray.add("330");

        typeArray.add("Small");
        typeArray.add("Large");
        typeArray.add("Medium");
        typeArray.add("Regular");
        typeArray.add("Sports");
        typeArray.add("Premium");
        typeArray.add("Small");
        typeArray.add("Premium");
        typeArray.add("Fancy");
        typeArray.add("Small");
        typeArray.add("Large");
        typeArray.add("Medium");
        typeArray.add("Regular");
        typeArray.add("Regular");
        typeArray.add("Small");
        typeArray.add("Large");
        typeArray.add("Medium");
        typeArray.add("Small");
        typeArray.add("Fancy");
        typeArray.add("Premium");

        colorArray.add("GunMetal");
        colorArray.add("Silver");
        colorArray.add("White");
        colorArray.add("Brown");
        colorArray.add("MidnightBlack");
        colorArray.add("BLACK GLOSS");
        colorArray.add("BURGUNDY MIST");
        colorArray.add("BUTTERSCOTCH");
        colorArray.add("CHERRY");
        colorArray.add("CLASSIC WHITE");
        colorArray.add("CREAM");
        colorArray.add("DAKOTA RED");
        colorArray.add("GLOSS YELLOW");
        colorArray.add("GOLD TOP");
        colorArray.add("NCA SILVER");
        colorArray.add("GunMetal");
        colorArray.add("METAL PELHAM BLUE");
        colorArray.add("PEARLY");
        colorArray.add("SHERWOOD GREEN");
        colorArray.add("SHELL PINK");

        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/Acc.jpg?alt=media&token=6be322b5-06fe-4f36-9543-3baa87e74747");

        addAccesssories();

    }

    public void add() {

        colorArray = new ArrayList<>();
        typeArray = new ArrayList<>();
        priceArray = new ArrayList<>();
        nameArray = new ArrayList<>();
        imageArray = new ArrayList<>();


        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/country-guitar-girl-music.jpg?alt=media&token=8f885cf5-412a-47f6-abdb-bac489208984");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/guitar-classical-guitar-acoustic-guitar-electric-guitar.jpg?alt=media&token=2469a418-ae68-4b3a-a76a-96a0b0925ed2");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/guitar-gibson-instrument-electric-159419.jpeg?alt=media&token=98a53313-cdb9-4d0c-8bbc-c9c645f9bbb4");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-1125744.jpeg?alt=media&token=28a9b8b0-a6db-4b0f-96c4-a4963d6c4bcd");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-164810.jpeg?alt=media&token=0de9c98f-af3f-4c30-addd-c503902a7b10");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-164826.jpeg?alt=media&token=c7c087b0-647b-437c-9bba-5ddf57c340ab");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-165971.jpeg?alt=media&token=53937b82-a5a3-4a43-8ae5-3b3bc1238e19");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-210765.jpeg?alt=media&token=e03d56c2-2e1a-49d9-9862-e8e5cb53c7b2");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-210835.jpeg?alt=media&token=2f80b1e2-a468-42d8-a5c1-23c68a1d9dbe");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-225230.jpeg?alt=media&token=fbecf9a4-2ef0-44ea-8f3b-57861a8cbb8c");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-346725.jpeg?alt=media&token=5e0b9a6c-4745-4c9c-b8cd-9c807c8eb560");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-625788.jpeg?alt=media&token=a371b02e-07a6-45d8-8d9b-5d01ac760935");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-68383.jpeg?alt=media&token=9162ee03-e161-4645-99c9-d59b227d75ee");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-713126.jpeg?alt=media&token=6bb61020-be69-4ebf-9f0b-c19eb82bcd9e");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-941674.png?alt=media&token=94fa658a-0c4a-415f-8245-a6ce60d7b929");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo_dsad.jpg?alt=media&token=3a26614d-1b2f-446c-91eb-46306a086ab9");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo.jpg?alt=media&token=3d7cea3b-fa47-4bec-a993-2570ca3b5970");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-713126.jpeg?alt=media&token=6bb61020-be69-4ebf-9f0b-c19eb82bcd9e");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/pexels-photo-210765.jpeg?alt=media&token=e03d56c2-2e1a-49d9-9862-e8e5cb53c7b2");
        imageArray.add("https://firebasestorage.googleapis.com/v0/b/rhytm-9d78d.appspot.com/o/guitar-classical-guitar-acoustic-guitar-electric-guitar.jpg?alt=media&token=2469a418-ae68-4b3a-a76a-96a0b0925ed2");


        nameArray.add("Harley Benton Parts Locking Tuners 6L Gold");
        nameArray.add("Blueridge");
        nameArray.add("Breedlove");
        nameArray.add("Charvel");
        nameArray.add("Daisy");
        nameArray.add("Dean");
        nameArray.add("Min");
        nameArray.add("Epiphone");
        nameArray.add("EVH");
        nameArray.add("Fender");
        nameArray.add("Godin");
        nameArray.add("Rickenbacker");
        nameArray.add("Rainsong");
        nameArray.add("PRS");
        nameArray.add("Ovation");
        nameArray.add("Martin");
        nameArray.add("Guild");
        nameArray.add("Godin");
        nameArray.add("Ibanez");
        nameArray.add("Jackson");


        priceArray.add("1500");
        priceArray.add("2000");
        priceArray.add("6000");
        priceArray.add("5000");
        priceArray.add("17000");
        priceArray.add("100000");
        priceArray.add("500000");
        priceArray.add("600000");
        priceArray.add("1200");
        priceArray.add("8000");
        priceArray.add("10000");
        priceArray.add("5000");
        priceArray.add("9000");
        priceArray.add("20000");
        priceArray.add("27000");
        priceArray.add("19000");
        priceArray.add("13000");
        priceArray.add("11000");
        priceArray.add("22000");
        priceArray.add("33000");


        typeArray.add("Acoustic");
        typeArray.add("Electric");
        typeArray.add("Twelve-string");
        typeArray.add("Electro-acoustic");
        typeArray.add("Archtop");
        typeArray.add("Steel");
        typeArray.add("Touch");
        typeArray.add("Bass");
        typeArray.add("Double-neck");
        typeArray.add("Acoustic");
        typeArray.add("Electro-acoustic");
        typeArray.add("Bass");
        typeArray.add("Double-neck");
        typeArray.add("GDouble-neck");
        typeArray.add("Bass");
        typeArray.add("Touch");
        typeArray.add("Steel");
        typeArray.add("Acoustic");
        typeArray.add("Twelve-string");
        typeArray.add("Twelve-string");


        colorArray.add("GunMetal");
        colorArray.add("Silver");
        colorArray.add("White");
        colorArray.add("Brown");
        colorArray.add("MidnightBlack");
        colorArray.add("BLACK GLOSS");
        colorArray.add("BURGUNDY MIST");
        colorArray.add("BUTTERSCOTCH");
        colorArray.add("CHERRY");
        colorArray.add("CLASSIC WHITE");
        colorArray.add("CREAM");
        colorArray.add("DAKOTA RED");
        colorArray.add("GLOSS YELLOW");
        colorArray.add("GOLD TOP");
        colorArray.add("NCA SILVER");
        colorArray.add("GunMetal");
        colorArray.add("METAL PELHAM BLUE");
        colorArray.add("PEARLY");
        colorArray.add("SHERWOOD GREEN");
        colorArray.add("SHELL PINK");

        addProducts();
    }*/


    /**
     * \
     * Redirect the user to login screen
     */
    public void redirectUser() {

        final String userId = getUserInfo();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(userId)){
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                }else{
                    startActivity(new Intent(SplashScreen.this, HomeScreen.class));
                }
                SplashScreen.this.finish();
            }
        }, 3000);

    }

    /*public void addProducts() {

        for (int i = 0; i < 20; i++) {
            GuitarDetailsModel guitarDetailsModel = new GuitarDetailsModel();
            String modelNo = String.valueOf(RandomNumberGenerator.getRandomNumber());
            guitarDetailsModel.setModelNo(modelNo);
            guitarDetailsModel.setColor(colorArray.get(i));
            guitarDetailsModel.setName(nameArray.get(i));
            guitarDetailsModel.setType(typeArray.get(i));
            guitarDetailsModel.setImage(imageArray.get(i));
            guitarDetailsModel.setPrice(priceArray.get(i));
            guitarDetailsModel.setAccessoriesModel(accessoriesModels.get(i));
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("guitars");
            // pushing user to 'users' node using the userId
            mDatabase.child(modelNo).setValue(guitarDetailsModel);
        }

        LoaderUtils.dismissProgress();
    }

    public void addAccesssories() {

        LoaderUtils.showProgressBar(SplashScreen.this, "Please wait...");

        for (int i = 0; i < 20; i++) {
            AccessoriesModel accessories = new AccessoriesModel();
            String modelNo = String.valueOf(RandomNumberGenerator.getRandomNumber());
            accessories.setModelNo(modelNo);
            accessories.setColor(colorArray.get(RandomNumberGenerator.getRandomNumberTwenty()));
            accessories.setName(nameArray.get(i));
            accessories.setType(typeArray.get(i));
            accessories.setImage(imageArray.get(0));
            accessories.setPrice(priceArray.get(i));
            accessoriesModels.add(accessories);
            //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("guitars");
            //pushing user to 'users' node using the userId
            //mDatabase.child(modelNo).setValue(guitarDetailsModel);
        }
        add();
    }*/

    public String getUserInfo(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getString("userId", null);
    }
}
