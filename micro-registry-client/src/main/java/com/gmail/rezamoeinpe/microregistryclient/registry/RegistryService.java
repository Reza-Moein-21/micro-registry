package com.gmail.rezamoeinpe.microregistryclient.registry;

import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;

import java.util.List;
import java.util.Optional;

public interface RegistryService {

    void sendHeartBeat(ServiceModel model);

    List<ServiceModel> findAll();

    List<ServiceModel> findAllIdeal();

    Optional<ServiceModel> findByName(ServiceModel example);

    List<ServiceModel> findAllSimilar(ServiceModel example);

    Optional<ServiceModel> deregister(String name);

    Optional<ServiceModel> deregister(ServiceModel mode);
}
