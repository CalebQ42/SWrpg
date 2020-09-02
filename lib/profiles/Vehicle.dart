import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Vehicle extends Editable{

  int silhouette = 0;
  int speed = 0;
  int handling = 0;
  int armor = 0;
  List<int> defense = new List();
  int totalDefense = 0;
  int hullTraumaThresh = 0;
  int hullTraumaCur = 0;
  int sysStressThresh = 0;
  int sysStressCur = 0;
  int encumCapacity = 0;
  int passengerCapacity = 0;
  int hp = 0;
  String model = "";

  String get fileExtension => ".swvehicle";
  List<String> get cardNames => [
    "Basic Information:",
    "Defense:",
    "Damage:",
    "Weapons:",
    "Critical Injuries:",
    "Description:"
  ];

  Vehicle({@required int id, String name = "New Vehicle", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Vehicle.load(FileSystemEntity file, SW app) : super.load(file, app);

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.silhouette = json["silhouette"];
    this.speed = json["speed"];
    this.handling = json["handling"];
    this.armor = json["armor"];
    defense = new List();
    for(dynamic i in json["defense"])
      defense.add(i);
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
    return List.filled(cardNames.length,
      EditableContent(builder: (bool b, refresh){
        return Text("Yo. It's a card");
      })
    );
  }

  static Vehicle of(BuildContext context) => Editable.of(context);
}