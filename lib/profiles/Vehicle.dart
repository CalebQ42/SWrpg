import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Vehicle extends Editable{

  int silhouette;
  int speed;
  int handling;
  int armor;
  List<int> defense;
  int totalDefense;
  int hullTraumaThresh;
  int hullTraumaCur;
  int sysStressThresh;
  int sysStressCur;
  int encumCapacity;
  int passengerCapacity;
  int hp;
  String model;

  int get cardNum => 6;
  String get fileExtension => ".swvehicle";

  Vehicle({@required int id, String name}) : super(id: id, name: name);

  Vehicle.fromJson(Map<String, dynamic> json) : super.fromJson(json){
    this.silhouette = json["silhouette"];
    this.speed = json["speed"];
    this.handling = json["handling"];
    this.armor = json["armor"];
    this.defense = json["defense"];
    this.totalDefense = json["total defense"];
    this.hullTraumaThresh  = json["hull trauma threshold"];
    this.hullTraumaCur = json["hull trauma current"];
    this.sysStressThresh = json["system stress threshold"];
    this.sysStressCur = json["system stress current"];
    this.encumCapacity = json["encumbrance capacity"];
    this.passengerCapacity = json["passenger capacity"];
    this.hp = json["hard points"];
    this.model = json["model"];
  }

  Vehicle.load(String filename) : super.load(filename);

  Map<String,dynamic> toJson(){
    var map = super.toJson();
    map["silhouette"] = silhouette;
    map["speed"] = speed;
    map["handling"] = handling;
    map["armor"] = armor;
    map["defense"] = defense;
    map["total defense"] = totalDefense;
    map["hull trauma threshold"] = hullTraumaThresh;
    map["hull trauma current"] = hullTraumaCur;
    map["system stress threshold"] = sysStressThresh;
    map["system stress current"] = sysStressCur;
    map["encumbrance capacity"] = encumCapacity;
    map["passenger capacity"] = passengerCapacity;
    map["hard points"] = hp;
    map["model"] = model;
    return map;
  }

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}