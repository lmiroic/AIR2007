<?php
class TipTransakcije{
    public $id;
    public $naziv;

    function __construct($redak){
        $this->id=$redak["id"];
        $this->naziv=$redak["naziv"];
    }
    function getJson()
    {        
        return array("id"=>$this->id,"naziv"=>$this->naziv);
    }
}
?>