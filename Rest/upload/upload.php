<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization");

 // include database connection file
require '../izvor/Database.class.php';
require '../entiteti/Transakcija.class.php';

$baza=new Database();
$baza->spojiDB();

//$data = json_decode(file_get_contents("php://input"), true); // collect input parameters and convert into readable format
function provjeriID(){
    return (isset($_POST["id"]))?true:false;
}
function provjeriAtribut(){
    return (isset($_POST["atribut"]))?true:false;
}
function provjeriVrijednost(){
    return (isset($_POST["vrijednost"]))?true:false;
}
function provjeriIznos(){
    return (isset($_POST["iznos"]))?true:false;
}
function provjeriDatum(){
    return (isset($_POST["datum"]))?true:false;
}
function provjeriRacunTerecenja(){
    return (isset($_POST["racunTerecenja"]))?true:false;
}
function provjeriRacunPrijenosa(){
    return (isset($_POST["racunPrijenosa"]))?true:false;
}
function provjeriTipTransakcije(){
    return (isset($_POST["tipTransakcije"]))?true:false;
}
function provjeriMemo(){
    return (isset($_POST["memo"]))?true:false;
}
function provjeriOpis(){
    return (isset($_POST["opis"]))?true:false;
}
function provjeriPonavljajuciTrosak(){
    return (isset($_POST["ponavljajuciTrosak"]))?true:false;
}
function provjeriIkonu(){
    return (isset($_POST["ikona"]))?true:false;
}
function provjeriKorisnika(){
    return (isset($_POST["korisnik"]))?true:false;
}
function provjeriIntervalPonavljanja(){
    return (isset($_POST["intervalPonavljanja"]))?true:false;
}
function provjeriKategorijuTransakcije(){
    return (isset($_POST["kategorijaTransakcije"]))?true:false;
}
function provjeriPlacenTrosak(){
    return (isset($_POST["placenTrosak"]))?true:false;
}

function provjeriPostojanostPodatakaNoveTransakcije(){
    return (provjeriIznos()&&provjeriDatum()&&provjeriRacunTerecenja()&&provjeriRacunPrijenosa()&&provjeriTipTransakcije()&&provjeriMemo()&&provjeriOpis()&&provjeriPonavljajuciTrosak()&&provjeriIkonu()&&provjeriKorisnika()&&provjeriIntervalPonavljanja()&&provjeriKategorijuTransakcije()&&provjeriPlacenTrosak())?true:false;
}

function provjeriPostojanostPodatakaAzuriraneTransakcije(){
    return (provjeriID()&&provjeriAtribut()&&provjeriVrijednost())?true:false;
}

function provjeriPostojanostPodatakaAzuriraneTransakcijeKorisnika(){
    return (provjeriID()&&provjeriIznos()&&provjeriDatum()&&provjeriRacunTerecenja()&&provjeriRacunPrijenosa()&&provjeriTipTransakcije()&&provjeriMemo()&&provjeriOpis()&&provjeriPonavljajuciTrosak()&&provjeriIkonu()&&provjeriKorisnika()&&provjeriIntervalPonavljanja()&&provjeriKategorijuTransakcije()&&provjeriPlacenTrosak())?true:false;
}

function kreirajRedakTransakcije($fileMemo){
    $redakTransakcije;
    $redakTransakcije["iznos"]=$_POST["iznos"];
    $redakTransakcije["datum"]=$_POST["datum"];
    $redakTransakcije["racunTerecenja"]=$_POST["racunTerecenja"];
    $redakTransakcije["racunPrijenosa"]=$_POST["racunPrijenosa"];
    $redakTransakcije["tipTransakcije"]=$_POST["tipTransakcije"];
    $redakTransakcije["memo"]=$fileMemo;
    $redakTransakcije["opis"]=$_POST["opis"];
    $redakTransakcije["ponavljajuciTrosak"]=$_POST["ponavljajuciTrosak"];
    $redakTransakcije["ikona"]=$_POST["ikona"];
    $redakTransakcije["korisnik"]=$_POST["korisnik"];
    $redakTransakcije["intervalPonavljanja"]=$_POST["intervalPonavljanja"];
    $redakTransakcije["kategorijaTransakcije"]=$_POST["kategorijaTransakcije"];
    $redakTransakcije["placenTrosak"]=$_POST["placenTrosak"];
    return $redakTransakcije;
}

if(isset($_GET["query"])&&$_GET["query"]=="insert"){
    
    $fileName  =  $_FILES['memo']['name'];
    $fileNameDate= date('dmYHis');
    $tempPath  =  $_FILES['memo']['tmp_name'];
    $fileSize  =  $_FILES['memo']['size'];
        
    if(empty($fileName))
    {
        $errorMSG = json_encode(array("message" => "please select image", "status" => false));    
        echo $errorMSG;
    }
    else
    {
        $upload_path = 'slike/'; // set upload folder path 
        
        $fileExt = strtolower(pathinfo($fileName,PATHINFO_EXTENSION)); // get image extension
        $fileNameDate=$fileNameDate.'.'.$fileExt;
            
        // valid image extensions
        $valid_extensions = array('jpeg', 'jpg', 'png', 'gif'); 
                        
        // allow valid image file formats
        if(in_array($fileExt, $valid_extensions))
        {                
            //check file not exist our upload folder path
            if(!file_exists($upload_path . $fileName))
            {
                // check file size '5MB'
                if($fileSize < 5000000){
                    move_uploaded_file($tempPath, $upload_path . $fileNameDate); // move file from system temporary path to our upload folder path
                    $folderPath='https://moneymakerair.tk/api/upload/slike/'.$fileNameDate; 
                    $transakcija = kreirajRedakTransakcije($folderPath);
                    $t = new Transakcija($transakcija);
                    $upit = "INSERT INTO transakcija(iznos,datum,racunTerecenja,racunPrijenosa,tipTransakcije,memo,opis,ponavljajuciTrosak,ikona,korisnik,intervalPonavljanja,kategorijaTransakcije,placenTrosak) VALUES (TRIM(BOTH '\"' FROM $t->iznos),TRIM(BOTH '\"' FROM '$t->datum'),TRIM(BOTH '\"' FROM $t->racunTerecenja),TRIM(BOTH '\"' FROM $t->racunPrijenosa),TRIM(BOTH '\"' FROM $t->tipTransakcije),TRIM(BOTH '\"' FROM '$t->memo'),TRIM(BOTH '\"' FROM '$t->opis'),TRIM(BOTH '\"' FROM $t->ponavljajuciTrosak),TRIM(BOTH '\"' FROM '$t->ikona'),TRIM(BOTH '\"' FROM $t->korisnik),TRIM(BOTH '\"' FROM '$t->intervalPonavljanja'),TRIM(BOTH '\"' FROM $t->kategorijaTransakcije),TRIM(BOTH '\"' FROM '$t->placenTrosak'))";
                    $rezultatObrade = $baza->updateDB($upit);
                    header('Content-type: application/json');
                    http_response_code(200); 
                    echo json_encode($t);
                    
                }
                else{        
                    $errorMSG = json_encode(array("message" => "Sorry, your file is too large, please upload 5 MB size", "status" => false));    
                    echo $errorMSG;
                }
            }
            else
            {        
                $errorMSG = json_encode(array("message" => "Sorry, file already exists check upload folder", "status" => false));    
                echo $errorMSG;
            }
        }
        else
        {        
            $errorMSG = json_encode(array("message" => "Sorry, only JPG, JPEG, PNG & GIF files are allowed", "status" => false));    
            echo $errorMSG;        
        }
    }
            
    /* if no error caused, continue ....
    if(!isset($errorMSG))
    {
        $query = mysqli_query($conn,'INSERT into tbl_image (name) VALUES("'.$fileName.'")');
                
        echo json_encode(array("message" => "Image Uploaded Successfully", "status" => true));    
    }*/

}

if(isset($_GET["query"])&&$_GET["query"]=="getall")
{
    $sveTransakcije=array();
    $DohvatIzBaze=$baza->selectDB("SELECT * from transakcija");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new Transakcija($redak,true);
        array_push($sveTransakcije,$t->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveTransakcije);
}  

if(isset($_GET["query"])&&$_GET["query"]=="selectOneTransakcija"&&isset($_GET["id"]))
{
    $redakTransakcije=array();
    $id=$_GET["id"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from transakcija where id=$id");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new Transakcija($redak,true);
        array_push($redakTransakcije,$t->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($redakTransakcije);
}

if(isset($_GET["query"])&&$_GET["query"]=="selectTransakcijeKorisnika"&&isset($_GET["korisnik"]))
{
    $sveTransakcijeKorisnika=array();
    $korisnik=$_GET["korisnik"];
    $DohvatIzBaze=$baza->selectDB("SELECT * from transakcija where korisnik=$korisnik");
    while($redak=mysqli_fetch_array($DohvatIzBaze))
    {
        $t=new Transakcija($redak,true);
        array_push($sveTransakcijeKorisnika,$t->getJson());
    }
    header('Content-type: application/json');
    http_response_code(200); 
    echo json_encode($sveTransakcijeKorisnika);
}

if(isset($_GET["query"])&&$_GET["query"]=="update"&&provjeriPostojanostPodatakaAzuriraneTransakcije()){

    $atribut = $_POST["atribut"];
    $vrijednost = $_POST["vrijednost"];
    $identifikator = $_POST["id"];
    $upit = "UPDATE transakcija SET $atribut='{$vrijednost}' WHERE id=$identifikator";
    $baza->updateDB($upit);
}

if(isset($_GET["query"])&&$_GET["query"]=="updateTransakcije"&&provjeriPostojanostPodatakaAzuriraneTransakcijeKorisnika()){

    $id = $_POST["id"];
    $iznos = $_POST["iznos"];
    $datum = $_POST["datum"];
    $racunTerecenja = $_POST["racunTerecenja"];
    $racunPrijenosa = $_POST["racunPrijenosa"];
    $tipTransakcije = $_POST["tipTransakcije"];
    $memo = $_POST["memo"];
    $opis = $_POST["opis"];
    $ponavljajuciTrosak = $_POST["ponavljajuciTrosak"];
    $ikona = $_POST["ikona"];
    $korisnik = $_POST["korisnik"];
    $intervalPonavljanja = $_POST["intervalPonavljanja"];
    $kategorijaTransakcije = $_POST["kategorijaTransakcije"];
    $placenTrosak = $_POST["placenTrosak"];
    $upit = "UPDATE transakcija SET iznos=$iznos,datum='$datum',racunTerecenja=$racunTerecenja,racunPrijenosa=$racunPrijenosa,tipTransakcije=$tipTransakcije,memo='$memo',opis='$opis',ponavljajuciTrosak=$ponavljajuciTrosak,ikona='$ikona',korisnik=$korisnik,intervalPonavljanja='$intervalPonavljanja',kategorijaTransakcije=$kategorijaTransakcije,placenTrosak=$placenTrosak WHERE id=$id";
    $baza->updateDB($upit);
}

if(isset($_GET["query"])&&$_GET["query"]=="delete"&&provjeriID()){
    $identifikator = $_POST["id"];
    $upit = "DELETE FROM transakcija WHERE id=$identifikator";
    $baza->updateDB($upit);
}

?>