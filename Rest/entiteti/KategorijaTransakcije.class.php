<?php
class KategorijaTransakcije{
    public $id;
    public $naziv;
    public $tip_transakcije;
    public $drawable;

    function __construct($redak){
        $this->id=$redak["id"];
        $this->naziv=$redak["naziv"];
        $this->tip_transakcije=$redak["tip_transakcije"];
        $this->drawable=$redak["drawable"];
    }
    function getJson()
    {        
        return array("id"=>$this->id,"naziv"=>$this->naziv,"tip_transakcije"=>$this->tip_transakcije,"drawable"=>$this->drawable);
    }
}
?>