package com.example.turystycznezaglebie;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.turystycznezaglebie.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    //private final Integer [] visit_time =  {15, 1, 3, 2, 4, 60, 25, 15, 60, 5, 50, 4, 20, 5, 20, 10, 25, 4, 4, 15, 45, 3, 2, 20, 5, 20, 3, 20, 120, 20, 10, 4, 90, 30, 10, 40, 7, 1, 2, 13, 8, 2, 2, 25, 3, 30, 30, 12, 45, 15, 4, 20};
    //private final Integer [] stars_rating = {5, 2, 3, 2, 3,  4,  3,  2,  5, 2,  3, 1,  3, 2,  4,  1,  3, 1, 3,  2,  2, 1, 1,  2, 2,  5, 1,  1,   3,  1,  2, 1, 4,  5,  3,  3,  2, 2, 1, 5,  4, 2, 3, 5,  2, 5,  4,  2,  5,  3,  2, 5};
    private Integer [] visit_time =   {13, 8, 2, 2, 25, 3, 30, 30, 12, 45, 15, 4, 20};
    private Integer [] stars_rating = {5,  4, 2, 3, 5,  2, 5,  4,  2,  5,  3,  2, 5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReadTxtFile fileReader = new ReadTxtFile();
        Integer [][] walk_matrix;
        Integer [][] car_matrix;
        walk_matrix = fileReader.readMatrix(getApplicationContext(), "data13D.txt", 13);
        car_matrix = fileReader.readMatrix(getApplicationContext(), "data13D_car.txt", 13);
        TravelData travelData = new TravelData(walk_matrix, visit_time, stars_rating, car_matrix);
        Experiment ex = new Experiment(visit_time, stars_rating, getApplicationContext());
        //ex.greedy_single();
        //ex.greedy_fihc_single();
        //ex.random_single();
        Greedy gr = new Greedy(travelData);
        CarSollution cs =  gr.findWayMultimodal(7, 240);
        //gr.findWay(7, 480);
        //SimulatedAnnealing sa = new SimulatedAnnealing(travelData, 0.99);
        //float sa_stars = sa.findWay(0, 120, 10);
        //RandomAlg randomAlg = new RandomAlg(travelData);
        //float rand_stars = randomAlg.findWay(0, 120, 5);
        //FirstImprovementHillClimber fihc = new FirstImprovementHillClimber(travelData);
        //fihc.improve(gr.getVisitedAttractions(),480);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sosnowiec and move the camera
        LatLng BazylikaSosnowiec = new LatLng(50.274672, 19.131766);
        LatLng Kiepura = new LatLng(50.27792544907462, 19.127158314567954);
        LatLng CerkiewSosnowiec = new LatLng(50.28108613166608, 19.1276734130083);
        LatLng DworzecSc = new LatLng(50.27860421665538, 19.126836512066433);
        LatLng KKolejowy = new LatLng(50.27933095754399, 19.128027435511967);
        LatLng KEwangelicki = new LatLng(50.2852333,19.1355321);
        LatLng PalacDietla = new LatLng(50.2852333,19.1355321);
        LatLng ParkDietlaGloteria = new LatLng(50.284934,19.1289857);
        LatLng MuzeumFarmacji = new LatLng(50.2791857,19.1250734);///zle
        LatLng CmZydowskiModrzejow = new LatLng(50.2362991,19.1479655);
        LatLng ParkBioroznorodnosci = new LatLng(50.2734261,19.1043024);
        LatLng ZamekSielecki = new LatLng(50.2853506,19.1434402);
        LatLng PalacSchoena = new LatLng(50.2997953,19.1406596);
        LatLng Tyranozaur = new LatLng(50.2997953,19.1406596);
        LatLng KJoahim = new LatLng(50.300358,19.1764942);
        LatLng GrodekRycerski = new LatLng(50.300358,19.1764942);
        LatLng DworzecMaczki = new LatLng(50.2620921,19.2651494);
        LatLng MiniZoo = new LatLng(50.2926943,19.2444644);
        //LatLng Srodulka = new LatLng(50.2752332,19.1315904);
        //LatLng KMatkiBozejBolesnej = new LatLng(50.2752332,19.1315904);
        //LatLng Piaski = new LatLng(50.2752332,19.1315904);
        //LatLng KStanislawa = new LatLng(50.2752332,19.1315904);
        //LatLng CzeladzRynek = new LatLng(50.2752332,19.1315904);
        //LatLng ObeliskSynagoga = new LatLng(50.2752332,19.1315904);
        //LatLng PomnikCzarownica = new LatLng(50.2752332,19.1315904);
        LatLng Saturn = new LatLng(50.3087993,19.0637429);
        LatLng Cmentarz4kultur = new LatLng(50.2848926,19.1235191);
        LatLng StadionCzarnych = new LatLng(50.2835556,19.1079933);
        LatLng KBarbary = new LatLng(50.2812336,19.1424866);
        LatLng Srodulka = new LatLng(50.2900642,19.1578522);
        LatLng Wapiennik = new LatLng(50.2681587,19.1681835);
        LatLng OsiedleCegielnia = new LatLng(50.2594639,19.148099);
        LatLng Trojkat3Cesarzy = new LatLng(50.2362995,19.1392107);
        LatLng MogilaPowstancowListopadowych = new LatLng(50.3000437,19.0910923);
        LatLng dworMieroszewski = new LatLng(50.300358,19.1764942);
        LatLng grotaMieroszewskich = new LatLng(50.300358,19.1764942);
        LatLng Torfowisko = new LatLng(50.2774257,19.277981);
        LatLng MostKolejowy = new LatLng(50.2610267,19.2634275);
        ////////////////////////////////////////////////////////////////////////////////////////////////
        mMap.addMarker(new MarkerOptions().position(BazylikaSosnowiec).title("Bazylika NMP w Sosnowcu"));
        mMap.addMarker(new MarkerOptions().position(Kiepura).title("Pomnik Jana Kiepury"));
        mMap.addMarker(new MarkerOptions().position(CerkiewSosnowiec).title("Cerkiew prawosławna pw. św. Wiery, Nadziei, Luby i matki ich Zofii"));
        mMap.addMarker(new MarkerOptions().position(KKolejowy).title("Kościółek kolejowy"));
        mMap.addMarker(new MarkerOptions().position(DworzecSc).title("Dworzec PKP w Sosnowcu"));
        mMap.addMarker(new MarkerOptions().position(KEwangelicki).title("Kościół Ewangelicko-Augsburdzki w Sosnowcu"));
        mMap.addMarker(new MarkerOptions().position(PalacDietla).title("Pałac Dietla"));
        mMap.addMarker(new MarkerOptions().position(ParkDietlaGloteria).title("Park Dietla"));
        mMap.addMarker(new MarkerOptions().position(MuzeumFarmacji).title("Muzeum Farmacji"));
        mMap.addMarker(new MarkerOptions().position(ParkBioroznorodnosci).title("Park Bioróżnorodności"));
        mMap.addMarker(new MarkerOptions().position(CmZydowskiModrzejow).title("Cmentarz Żydowski"));
        mMap.addMarker(new MarkerOptions().position(ZamekSielecki).title("Zamek Sielecki"));
        mMap.addMarker(new MarkerOptions().position(PalacSchoena).title("Palac Schoena"));
        mMap.addMarker(new MarkerOptions().position(Tyranozaur).title("Tyranozaur (Zuzia)"));
        mMap.addMarker(new MarkerOptions().position(KJoahim).title("Kościół pw. św. Joahima"));
        mMap.addMarker(new MarkerOptions().position(GrodekRycerski).title("Gródek rycerski"));
        mMap.addMarker(new MarkerOptions().position(DworzecMaczki).title("Dworzec na Maczkach"));
        mMap.addMarker(new MarkerOptions().position(MiniZoo).title("Mini zoo"));
        mMap.addMarker(new MarkerOptions().position(Srodulka).title("Górka Środulska"));
        //mMap.addMarker(new MarkerOptions().position(KMatkiBozejBolesnej).title("Kościół pw. Matki Bożej Bolesnej"));
        //mMap.addMarker(new MarkerOptions().position(Piaski).title("Kolonia górnicza Piaski"));
        //mMap.addMarker(new MarkerOptions().position(KStanislawa).title("Kościół pw. św. Stanisława biskupa i męczennika"));
        //mMap.addMarker(new MarkerOptions().position(CzeladzRynek).title("Rynek w Czeladzi"));
        //mMap.addMarker(new MarkerOptions().position(ObeliskSynagoga).title("Obelisk w miejscu dawnej synagogi"));
        //mMap.addMarker(new MarkerOptions().position(PomnikCzarownica).title("Pomnik Czarownicy"));
        mMap.addMarker(new MarkerOptions().position(Saturn).title("Galeria sztuki współczesnej w dawnej kopalni Saturn"));
        mMap.addMarker(new MarkerOptions().position(MiniZoo).title("Mini zoo"));
        //mMap.addMarker(new MarkerOptions().position(MiniZoo).title("Mini zoo"));
        //mMap.addMarker(new MarkerOptions().position(MiniZoo).title("Mini zoo"));
        //mMap.addMarker(new MarkerOptions().position(MiniZoo).title("Mini zoo"));
        mMap.setBuildingsEnabled(true);
        mMap.setMinZoomPreference(10);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.277537,19.1284039), 12));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BazylikaSosnowiec));
    }
}