<?php
class Valuta{
    public $naziv;
    public $tecaj;
    public $datum;

    function __construct($redak,$identifikator=false){
        if($identifikator){
            $this->naziv=$redak["naziv"];
        }   
        $this->tecaj=$redak["tecaj"];
        $this->datum=$redak["datum"];
    }
    function getJson()
    {
        return array("naziv"=>$this->naziv,"tecaj"=>$this->tecaj,"datum"=>$this->datum);
    }
}
?>