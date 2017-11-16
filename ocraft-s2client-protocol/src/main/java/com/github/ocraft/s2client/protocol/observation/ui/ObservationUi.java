package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class ObservationUi implements Serializable {

    private static final long serialVersionUID = -2959958878592615367L;

    private final Set<ControlGroup> controlGroups;
    private final SinglePanel singlePanel;
    private final MultiPanel multiPanel;
    private final CargoPanel cargoPanel;
    private final ProductionPanel productionPanel;

    private ObservationUi(Ui.ObservationUI sc2ApiObservationUi) {
        controlGroups = sc2ApiObservationUi.getGroupsList().stream().map(ControlGroup::from).collect(toSet());

        singlePanel = tryGet(Ui.ObservationUI::getSingle, Ui.ObservationUI::hasSingle)
                .apply(sc2ApiObservationUi).map(SinglePanel::from).orElse(nothing());

        multiPanel = tryGet(Ui.ObservationUI::getMulti, Ui.ObservationUI::hasMulti)
                .apply(sc2ApiObservationUi).map(MultiPanel::from).orElse(nothing());

        cargoPanel = tryGet(Ui.ObservationUI::getCargo, Ui.ObservationUI::hasCargo)
                .apply(sc2ApiObservationUi).map(CargoPanel::from).orElse(nothing());

        productionPanel = tryGet(Ui.ObservationUI::getProduction, Ui.ObservationUI::hasProduction)
                .apply(sc2ApiObservationUi).map(ProductionPanel::from).orElse(nothing());
    }

    public static ObservationUi from(Ui.ObservationUI sc2ApiObservationUi) {
        require("sc2api observation ui", sc2ApiObservationUi);
        return new ObservationUi(sc2ApiObservationUi);
    }

    public Set<ControlGroup> getControlGroups() {
        return new HashSet<>(controlGroups);
    }

    public Optional<SinglePanel> getSinglePanel() {
        return Optional.ofNullable(singlePanel);
    }

    public Optional<MultiPanel> getMultiPanel() {
        return Optional.ofNullable(multiPanel);
    }

    public Optional<CargoPanel> getCargoPanel() {
        return Optional.ofNullable(cargoPanel);
    }

    public Optional<ProductionPanel> getProductionPanel() {
        return Optional.ofNullable(productionPanel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservationUi that = (ObservationUi) o;

        return controlGroups.equals(that.controlGroups) &&
                (singlePanel != null ? singlePanel.equals(that.singlePanel) : that.singlePanel == null) &&
                (multiPanel != null ? multiPanel.equals(that.multiPanel) : that.multiPanel == null) &&
                (cargoPanel != null ? cargoPanel.equals(that.cargoPanel) : that.cargoPanel == null) &&
                (productionPanel != null ? productionPanel.equals(that.productionPanel) : that.productionPanel == null);
    }

    @Override
    public int hashCode() {
        int result = controlGroups.hashCode();
        result = 31 * result + (singlePanel != null ? singlePanel.hashCode() : 0);
        result = 31 * result + (multiPanel != null ? multiPanel.hashCode() : 0);
        result = 31 * result + (cargoPanel != null ? cargoPanel.hashCode() : 0);
        result = 31 * result + (productionPanel != null ? productionPanel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
