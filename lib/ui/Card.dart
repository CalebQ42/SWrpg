import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class InfoCard extends StatefulWidget{

  final InfoCardHolder holder = new InfoCardHolder();
  final Function onHide;

  InfoCard({shown = true, @required Widget contents, String title = "", this.onHide}){
    holder.shown = shown;
    holder.contents = contents;
    holder.title = title;
  }

  @override
  State<StatefulWidget> createState() {
    return InfoCardState(holder);
  }
}

class InfoCardState extends State{

  InfoCardHolder holder;
  Function onHide;

  InfoCardState(this.holder);

  @override
  Widget build(BuildContext context) {
    var content;
    if(holder.shown)
      content = holder.contents;
    else
      content = Container();
    return Card(
      child: Padding(
        padding: EdgeInsets.all(10.0),
        child: Column(
          children: <Widget>[
            SwitchListTile(
              title: Text(holder.title),
              value: holder.shown,
              onChanged: (bool b){
                setState((){
                  onHide();
                  holder.shown = b;
                });
              },
            ),
            new AnimatedSwitcher(
              duration: Duration(milliseconds:250),
              transitionBuilder: (Widget wid,Animation<double> anim){
                return SizeTransition(
                  axis:Axis.vertical,
                  sizeFactor: anim,
                  child: wid,
                  axisAlignment: -1.0
                );
              },
              child: content
            )
          ],
        )
      )
    );
  }
}

class InfoCardHolder{
  bool shown;
  Widget contents;
  String title;
}

class NameCardContent extends StatefulWidget{
  final Editable editable;

  const NameCardContent(this.editable);
  @override
  State<StatefulWidget> createState() {
    return NameCardContentState(editable);
  }
}

class NameCardContentState extends State{

  Editable editable;
  bool editing = false;

  NameCardContentState(this.editable);
  @override
  Widget build(BuildContext context) {
    TextEditingController controller;
    if(editing){
      controller = new TextEditingController(text: editable.name);
      controller.addListener(() {
        editable.name = controller.text;
      });
    }
    return Column(
      children: <Widget>[
        EditingText(
          editing: editing,
          style: Theme.of(context).textTheme.headline5,
          initialText: editable.name,
          controller: controller),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            IconButton(
              icon:Icon(Icons.edit),
              iconSize: 25.0,
              onPressed: (){
                setState( ()=> editing = !editing);
              }
            )
          ],
        )
      ],
    );
  }
}