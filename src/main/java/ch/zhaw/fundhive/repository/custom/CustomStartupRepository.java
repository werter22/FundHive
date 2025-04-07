package ch.zhaw.fundhive.repository.custom;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;

import java.util.List;

public interface CustomStartupRepository {
    List<Startup> filterStartups(
            IndustryType industry,
            StartupFundingStatus fundingStatus,
            Double minValuation,
            Double maxValuation);
}
