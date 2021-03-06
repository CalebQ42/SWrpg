import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Obligation.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/dialogs/character/ObligationEditDialog.dart';

class Obligations extends StatelessWidget{

  final Function() refresh;
  final bool editing;

  Obligations({required this.refresh, required this.editing});

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null)
      throw "Obligations card used on non Character";
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.obligations.length,
          (index) => InkResponse(
              containedInkWell: true,
              highlightShape: BoxShape.rectangle,
              child: Row(
                children: [
                  Expanded(
                    child: Text(character.obligations[index].name)
                  ),
                  AnimatedSwitcher(
                    child: editing ? ButtonBar(
                      buttonPadding: EdgeInsets.zero,
                      children: [
                        IconButton(
                          icon: Icon(Icons.delete_forever),
                          iconSize: 24.0,
                          constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                          onPressed: (){
                            var tmp = Obligation.from(character.obligations[index]);
                            character.obligations.removeAt(index);
                            refresh();
                            character.save(context: context);
                            ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                              content: Text("Deleted Obligation"),
                              action: SnackBarAction(
                                label: "Undo",
                                onPressed: (){
                                  character.obligations.insert(index, tmp);
                                  refresh();
                                  character.save(context: context);
                                },
                              ),
                            ));
                          },
                        ),
                        IconButton(
                          icon: Icon(Icons.edit),
                          iconSize: 24.0,
                          constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                          onPressed: () =>
                            ObligationEditDialog(
                              obligation: character.obligations[index],
                              onClose: (obligation){
                                character.obligations[index] = obligation;
                                refresh();
                                character.save(context: context);
                              },
                            ).show(context)
                        )
                      ],
                    ) : Padding(
                      child:Text(character.obligations[index].value.toString()),
                      padding: EdgeInsets.all(12)
                    ),
                    duration: Duration(milliseconds: 250),
                    transitionBuilder: (child, anim){
                      var offset = Offset(1,0);
                      if((!editing && child is ButtonBar) || (editing && child is Padding))
                        offset = Offset(-1,0);
                      return ClipRect(
                        child: SizeTransition(
                          sizeFactor: anim,
                          axis: Axis.horizontal,
                          child: SlideTransition(
                            position: Tween<Offset>(
                              begin: offset,
                              end: Offset.zero
                            ).animate(anim),
                            child: child,
                          )
                        )
                      );
                    },
                  )
                ]
              ),
              onTap: () =>
                showModalBottomSheet(
                  context: context,
                  builder: (context) =>
                    Padding(
                      padding: EdgeInsets.only(left: 10, right: 10, bottom: 20),
                      child: Wrap(
                        alignment: WrapAlignment.center,
                        children: [
                          Container(height: 15),
                          Text(
                            character.obligations[index].name,
                            style: Theme.of(context).textTheme.headline5,
                            textAlign: TextAlign.center
                          ),
                          Container(height: 5),
                          Text(
                            character.obligations[index].value.toString() + " Obligation",
                            style: Theme.of(context).textTheme.bodyText1,
                            textAlign: TextAlign.center,
                          ),
                          Container(height: 10),
                          if(character.obligations[index].desc != "") Text(character.obligations[index].desc)
                        ],
                      )
                    )
                )
          )
        )..add(
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            transitionBuilder: (child, anim) =>
              SizeTransition(
                sizeFactor: anim,
                child: child,
                axisAlignment: -1.0
              ),
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: () =>
                  ObligationEditDialog(
                    onClose: (obligation){
                      character.obligations.add(obligation);
                      refresh();
                      character.save(context: context);
                    }
                  ).show(context),
              )
            ) : Container(),
          )
        )
      )
    );
  }
}