### Prerequisites

```bash
cd <root-xsk>
mvn clean install -DskipTests=true
```

### Manual Build

1. Build `Kneo Java Stack`:

    ```
    docker build -t dirigiblelabs/kneo-java-stack-base . --target base
    docker push dirigiblelabs/kneo-java-stack-base

    docker build -t dirigiblelabs/kneo-java-stack-run . --target run
    docker push dirigiblelabs/kneo-java-stack-run

    docker build -t dirigiblelabs/kneo-java-stack-build . --target build
    docker push dirigiblelabs/kneo-java-stack-build
    ```

1. Build `Kneo Java Buildpack`:

    ```
    cd buildpack/

    pack buildpack package dirigiblelabs/kneo-java-buildpack --config ./package.toml
    docker push dirigiblelabs/kneo-java-buildpack
    ```

### Kpack Installation

1. [Install Pack](https://buildpacks.io/docs/tools/pack/#install)
1. [Install Kpack](https://github.com/pivotal/kpack/blob/main/docs/install.md)
1. [Install logging tool](https://github.com/pivotal/kpack/blob/main/docs/logs.md)
1. Create Docker Registry Secret:
    ```
    kubectl create secret docker-registry docker-registry-secret \
        --docker-username=<your-username> \
        --docker-password=<your-password> \
        --docker-server=https://index.docker.io/v1/ \
        --namespace default
    ```

1. Create Service Account
    ```
    kubectl apply -f service-account.yaml
    ```

1. Create `ClusterStore`, `ClusterStack` and `Builder`:

    ```
    kubectl apply -f kpack.yaml
    ```

### Image Building

1. Create Image:

    ```yaml
    apiVersion: kpack.io/v1alpha1
    kind: Image
    metadata:
      name: java-application
      namespace: default
    spec:
      tag: dirigiblelabs/java-application
      serviceAccount: docker-registry-service-account
      builder:
        name: kneo-java
        kind: Builder
      source:
        blob:
          url: https://github.com/SAP/xsk/raw/main/samples/java-jdbc.war.zip
    ```

1. Monitor Logs:

    ```
    logs -image java-application -namespace default
    ```
