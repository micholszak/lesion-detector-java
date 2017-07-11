package pl.olszak.michal.detector.utils;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * @author molszak
 *         created on 11.07.2017.
 */
public class CallbackProvider {

    private final Callback<ListView<ColorReduce>, ListCell<ColorReduce>> comboBoxColorReduceCellCallback = new Callback<ListView<ColorReduce>, ListCell<ColorReduce>>() {
        @Override
        public ListCell<ColorReduce> call(ListView<ColorReduce> param) {
            return new ListCell<ColorReduce>() {
                {
                    super.setPrefHeight(60);
                }

                @Override
                protected void updateItem(ColorReduce item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.toString());
                        setTextFill(Color.BLACK);
                    }
                }
            };
        }
    };

    public Callback<ListView<ColorReduce>, ListCell<ColorReduce>> provideComboBoxCallback() {
        return comboBoxColorReduceCellCallback;
    }


}
