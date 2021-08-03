package ethos.model.content.group_ironman.sorts;

import ethos.model.content.group_ironman.IronmanTeam;

import java.util.Comparator;

/**
 * Ironman date sorting
 * @author optimum on 14/05/2020
 */
public class IronmanTeamDateSort implements Comparator<IronmanTeam> {

    @Override
    public int compare(IronmanTeam o1, IronmanTeam o2) {
        return o1.getDateStated().compareTo(o2.getDateStated());
    }

    public static final IronmanTeamDateSort DATE_SORT = new IronmanTeamDateSort();
}
