<?php
class Racun{
    public $id;
    public $naziv;
    public $pocetno_stanje;
    public $valuta;
    public $ikona;

    function __construct($redak,$identifikator=false){
        if($identifikator){
            $this->id=$redak["id"];
        }        
        $this->naziv=$redak["naziv"];
        $this->pocetno_stanje=$redak["pocetno_stanje"];
        $this->valuta=$redak["valuta"];
        $this->ikona=$redak["ikona"];
    }
    function getJson()
    {
        if($this->id!=""){
            return array("id"=>$this->id,"naziv"=>$this->naziv,"pocetno_stanje"=>$this->pocetno_stanje,"valuta"=>$this->valuta,"ikona"=>$this->ikona);
        }
        else{
            return array("naziv"=>$this->naziv,"pocetno_stanje"=>$this->pocetno_stanje,"valuta"=>$this->valuta,"ikona"=>$this->ikona);
        }
    }
}
?>