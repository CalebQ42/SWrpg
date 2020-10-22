import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_speed_dial/flutter_speed_dial.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';

class EditableList extends StatefulWidget{

  //This determines which list is shown
  static final character = 0;
  static final minion = 1;
  static final vehicle = 2;

  final int type;

  EditableList(this.type){
    if(this.type <0 || this.type > 2)
      throw("Invlid type");
  }

  @override
  State<StatefulWidget> createState() => _EditableListState(type);
}

class _EditableListState extends State{

  int type;
  List list;
  String category;
  // bool open = false;

  _EditableListState(this.type);

  Widget build(BuildContext context) {
    switch(type){
      case 0:
        list = SW.of(context).characters(category: category);
        break;
      case 1:
        list = SW.of(context).minions(category: category);
        break;
      case 2:
        list = SW.of(context).vehicles(category: category);
        break;
      default:
        throw("invalid list type");
    }
    return Scaffold(
      drawer: SWDrawer(),
      appBar: SWAppBar(
        title: Text(
          (){
            switch(type){
              case 0:
                return "Characters";
                break;
              case 1:
                return "Minions";
                break;
              case 2:
                return "Vehicles";
                break;
              default:
                return "";
            }
          }(),
        ),
        bottom: PreferredSize(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 10.0),
            child: DropdownButtonHideUnderline(
              child: DropdownButton<String>(
                items: (){
                  List<String> cats;
                  switch(type){
                    case 0:
                      cats = SW.of(context).charCats;
                      break;
                    case 1:
                      cats = SW.of(context).minCats;
                      break;
                    case 2:
                      cats =  SW.of(context).vehCats;
                      break;
                  }
                  List<DropdownMenuItem<String>> out = List();
                  out.add(DropdownMenuItem(
                    child: Text("All"),
                    value: null
                  ));
                  out.add(DropdownMenuItem(
                    child: Text("Uncategorized"),
                    value: ""
                  ));
                  cats.forEach((element) {
                    out.add(DropdownMenuItem<String>(
                      child: Text(element),
                      value: element
                    ));
                  });
                  return out;
                }(),
                hint: Text("Categories"),
                onChanged: (String value) {
                  setState(() => category = value);
                },
                value: category,
                isExpanded: true,
              )
            )
          ),
          preferredSize: Size.fromHeight(50),
        ),
      ),
      body: ListView.builder(
        itemCount: list.length,
        itemBuilder: (BuildContext c, int index) => 
          EditableCard(editable: list[index], refreshCallback: () => setState((){}), upperContext: c,),
      ),
      //TODO: implement profile creator
      // floatingActionButton: SpeedDial(
      //   child: Icon(Icons.add),
      //   onClose: () => setState(() => open = false),
      //   onOpen: () => setState(() => open = true),
      //   children: [
      //     SpeedDialChild(
      //       label: "Blank",
      //       child: Icon(Icons.add),
      //       onTap: (){
      //         var id = 0;
      //         switch (type){
      //           case 0:
      //             while(SW.of(context).characters().any((e)=>e.id==id))
      //               id++;
      //             setState(() => SW.of(context).add(new Character(id: id, saveOnCreation: true, app: SW.of(context))));
      //             break;
      //           case 1:
      //             while(SW.of(context).minions().any((e)=>e.id==id))
      //               id++;
      //             setState(() => SW.of(context).add(new Minion(id: id, saveOnCreation: true, app: SW.of(context))));
      //             break;
      //           case 2:
      //             while(SW.of(context).vehicles().any((e)=>e.id==id))
      //               id++;
      //             setState(() => SW.of(context).add(new Vehicle(id: id, saveOnCreation: true, app: SW.of(context))));
      //             break;
      //         }
      //       }
      //     ),
      //     SpeedDialChild(
      //       label: "Creator",
      //       child: Icon(Icons.build),
      //       onTap: (){
      //         //TODO: character creator
      //       }
      //     )
      //   ],
      // ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: (){
          var id = 0;
          switch (type){
            case 0:
              while(SW.of(context).characters().any((e)=>e.id==id))
                id++;
              setState(() => SW.of(context).add(new Character(id: id, saveOnCreation: true, app: SW.of(context))));
              break;
            case 1:
              while(SW.of(context).minions().any((e)=>e.id==id))
                id++;
              setState(() => SW.of(context).add(new Minion(id: id, saveOnCreation: true, app: SW.of(context))));
              break;
            case 2:
              while(SW.of(context).vehicles().any((e)=>e.id==id))
                id++;
              setState(() => SW.of(context).add(new Vehicle(id: id, saveOnCreation: true, app: SW.of(context))));
              break;
          }
        }
      ),
    );
  }
}

class EditableCard extends StatelessWidget{
  final Key key;
  final Editable editable;
  final Function refreshCallback;
  final BuildContext upperContext;

  EditableCard({this.key, this.editable, this.refreshCallback, this.upperContext});
  @override
  Widget build(BuildContext context) =>
    Dismissible(
      key: UniqueKey(),
      onDismissed: (direction){
        var tmp = editable;
        editable.delete(SW.of(upperContext));
        SW.of(upperContext).remove(editable, context);
        String msg;
        if(editable is Character)
          msg = "Character Deleted";
        else if (editable is Minion)
          msg = "Minion Deleted";
        else if (editable is Vehicle)
          msg = "Vehicle Deleted";
        Scaffold.of(upperContext).showSnackBar(
          SnackBar(
            content: Text(msg),
            action: SnackBarAction(
              label: "Undo",
              onPressed: (){
                SW.of(upperContext).add(tmp);
                tmp.save(context: context);
                refreshCallback();
              }
            ),
          )
        );
        refreshCallback();
      },
      child: InheritedEditable(
        child: Card(
          child: InkResponse(
            onTap: (){
              if(editable.route != null && SW.of(context).observatory.containsRoute(editable.route))
                Navigator.replace(context, oldRoute: editable.route, newRoute: editable.setRoute(refreshCallback));
              else
                Navigator.push(context, editable.setRoute(refreshCallback));
            },
            child:Padding(
              padding: EdgeInsets.all(10.0),
              child: Hero(
                transitionOnUserGestures: true,
                tag: (){
                  String out;
                  if(editable is Character)
                    out = "character/";
                  else if(editable is Minion)
                    out = "minion/";
                  else if(editable is Vehicle)
                    out = "vehicle/";
                  
                  return out + editable.id.toString();
                }(),
                child: Text(editable.name, style: Theme.of(context).textTheme.headline5, textAlign: TextAlign.center,)
              ),
            )
          )
        ),
        editable: editable
      )
    );
}