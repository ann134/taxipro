package ru.sigmadigital.taxipro.repo;

import androidx.lifecycle.MutableLiveData;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.GeoAddress;

public class ProfileRepository {

    private static ProfileRepository instance;
    private MutableLiveData<Driver.Profile> profileLiveData = new MutableLiveData<>();

    public static ProfileRepository getInstance() {
        if (instance == null) {
            instance = new ProfileRepository();
        }
        return instance;
    }

    public void setProfile(Driver.Profile profileLiveData) {
        this.profileLiveData.setValue(profileLiveData);
    }

    public Driver.Profile getProfile() {
        return profileLiveData.getValue();
    }

    public MutableLiveData<Driver.Profile> getProfileLiveData() {
        return profileLiveData;
    }



    //set status
    public void setStateFromAsync(Driver.StateHasBeenModifiedAsync newState) {
        setState(newState.getState());
    }

    public void setStateOnline(){
        setState(Driver.DriverState.online);
    }

    public void setStateOffline(){
        setState(Driver.DriverState.offline);
    }

    public void setStateHome(){
        setState(Driver.DriverState.onWayHome);
    }

    private void setState (int state) {
        Driver.Profile profile = profileLiveData.getValue();
        if (profile != null){
            profile.setState(state);
            profileLiveData.setValue(profile);
        }
    }


    //geoAddress

    private GeoAddress geoAddressHome;

    public GeoAddress getGeoAddressHome() {
        return geoAddressHome;
    }

    public void setGeoAddressHome(GeoAddress geoAddressHome) {
        this.geoAddressHome = geoAddressHome;
    }



    //set sos
    public void setSos (boolean isSos) {
        Driver.Profile profile = profileLiveData.getValue();
        if (profile != null){
            profile.setSos(isSos);
            profileLiveData.setValue(profile);
        }
    }

    //activity

    private int activism;

    public int getActivism() {
        return activism;
    }

    public void setActivism(int activism) {
        this.activism = activism;
    }


    //city
    public void setCity(int id){
        Driver.Profile profile = profileLiveData.getValue();
        if (profile != null){
            profile.setCityId(id);
            profileLiveData.setValue(profile);
        }
    }

}
