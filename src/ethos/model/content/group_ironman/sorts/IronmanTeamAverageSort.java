package ethos.model.content.group_ironman.sorts;

import ethos.model.content.group_ironman.IronmanTeam;

import java.util.Comparator;

/**
 * Ironman default sort by getAverageCombatLevel
 *
 * @author optimum on 14/05/2020
 */
public class IronmanTeamAverageSort implements Comparator<IronmanTeam> {

    @Override
    public int compare(IronmanTeam o1, IronmanTeam o2) {
        return Integer.compare(o2.getAverageTotalLevel(), o1.getAverageTotalLevel());
    }


    public static final IronmanTeamAverageSort AVERAGE_SORT = new IronmanTeamAverageSort();
}
