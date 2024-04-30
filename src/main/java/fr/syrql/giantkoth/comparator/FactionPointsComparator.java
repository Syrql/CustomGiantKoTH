package fr.syrql.giantkoth.comparator;

import fr.syrql.giantkoth.data.FactionPoints;

import java.util.Comparator;

public class FactionPointsComparator implements Comparator<FactionPoints> {

    @Override
    public int compare(FactionPoints o1, FactionPoints o2) {
        return Integer.compare(o2.getPoints(), o1.getPoints());
    }
}