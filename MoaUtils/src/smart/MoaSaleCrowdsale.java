package smart.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.3.1.
 */
public final class MoaSaleCrowdsale extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405160608061089a8339810160409081528151602083015191909201518282826000831161003f57600080fd5b600160a060020a038216151561005457600080fd5b600160a060020a038116151561006957600080fd5b60029290925560018054600160a060020a03928316600160a060020a03199182161790915560008054938316938216939093179092556004805433909216919092161790555050506107da806100c06000396000f3006080604052600436106100cf5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416632c4e722e81146100da5780634042b66f14610101578063412664ae1461011657806342966c681461013a578063521eb27314610152578063715018a6146101835780638ab1d681146101985780638c10671c146101b95780638da5cb5b146101d95780639b19251a146101ee578063e43252d714610223578063ec8ac4d814610244578063f2fde38b14610258578063fc0c546a14610279575b6100d83361028e565b005b3480156100e657600080fd5b506100ef61033b565b60408051918252519081900360200190f35b34801561010d57600080fd5b506100ef610341565b34801561012257600080fd5b506100d8600160a060020a0360043516602435610347565b34801561014657600080fd5b506100d8600435610404565b34801561015e57600080fd5b50610167610480565b60408051600160a060020a039092168252519081900360200190f35b34801561018f57600080fd5b506100d861048f565b3480156101a457600080fd5b506100d8600160a060020a0360043516610501565b3480156101c557600080fd5b506100d8600480356024810191013561053d565b3480156101e557600080fd5b506101676105b2565b3480156101fa57600080fd5b5061020f600160a060020a03600435166105c1565b604080519115158252519081900360200190f35b34801561022f57600080fd5b506100d8600160a060020a03600435166105d6565b6100d8600160a060020a036004351661028e565b34801561026457600080fd5b506100d8600160a060020a0360043516610615565b34801561028557600080fd5b506101676106ae565b34600061029b83836106bd565b6102a4826106f0565b6003549091506102ba908363ffffffff61070d16565b6003556102c7838261071a565b82600160a060020a031633600160a060020a03167f623b3804fa71d67900d064613da8f94b9617215ee90799290593e1745087ad188484604051808381526020018281526020019250505060405180910390a36103248383610724565b61032c610728565b6103368383610724565b505050565b60025481565b60035481565b60045433600160a060020a0390811691161461036257600080fd5b60008054604080517fa9059cbb000000000000000000000000000000000000000000000000000000008152600160a060020a038681166004830152602482018690529151919092169263a9059cbb92604480820193602093909283900390910190829087803b1580156103d457600080fd5b505af11580156103e8573d6000803e3d6000fd5b505050506040513d60208110156103fe57600080fd5b50505050565b60008054604080517f42966c68000000000000000000000000000000000000000000000000000000008152600481018590529051600160a060020a03909216926342966c689260248084019382900301818387803b15801561046557600080fd5b505af1158015610479573d6000803e3d6000fd5b5050505050565b600154600160a060020a031681565b60045433600160a060020a039081169116146104aa57600080fd5b600454604051600160a060020a03909116907ff8df31144d9c2f0f6b59d69b8b98abd5459d07f2742c4df920b25aae33c6482090600090a26004805473ffffffffffffffffffffffffffffffffffffffff19169055565b60045433600160a060020a0390811691161461051c57600080fd5b600160a060020a03166000908152600560205260409020805460ff19169055565b60045460009033600160a060020a0390811691161461055b57600080fd5b5060005b818110156103365760016005600085858581811061057957fe5b60209081029290920135600160a060020a0316835250810191909152604001600020805460ff191691151591909117905560010161055f565b600454600160a060020a031681565b60056020526000908152604090205460ff1681565b60045433600160a060020a039081169116146105f157600080fd5b600160a060020a03166000908152600560205260409020805460ff19166001179055565b60045433600160a060020a0390811691161461063057600080fd5b600160a060020a038116151561064557600080fd5b600454604051600160a060020a038084169216907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a36004805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b600054600160a060020a031681565b600160a060020a038216600090815260056020526040902054829060ff1615156106e657600080fd5b6103368383610764565b60006107076002548361078590919063ffffffff16565b92915050565b8181018281101561070757fe5b6107248282610362565b5050565b600154604051600160a060020a03909116903480156108fc02916000818181858888f19350505050158015610761573d6000803e3d6000fd5b50565b600160a060020a038216151561077957600080fd5b80151561072457600080fd5b600082151561079657506000610707565b508181028183828115156107a657fe5b041461070757fe00a165627a7a72305820d0497f74ad4ef8675e7fadc3c664afc2da3142e45e74738d089eeb086c028c620029";

    private MoaSaleCrowdsale(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private MoaSaleCrowdsale(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<OwnershipRenouncedEventResponse> getOwnershipRenouncedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnershipRenounced", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnershipRenouncedEventResponse> responses = new ArrayList<OwnershipRenouncedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnershipRenouncedEventResponse typedResponse = new OwnershipRenouncedEventResponse();
            typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipRenouncedEventResponse> ownershipRenouncedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnershipRenounced", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipRenouncedEventResponse>() {
            @Override
            public OwnershipRenouncedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnershipRenouncedEventResponse typedResponse = new OwnershipRenouncedEventResponse();
                typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("OwnershipTransferred", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("OwnershipTransferred", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.previousOwner = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.newOwner = (Address) eventValues.getIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public List<TokenPurchaseEventResponse> getTokenPurchaseEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TokenPurchase", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<TokenPurchaseEventResponse> responses = new ArrayList<TokenPurchaseEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            TokenPurchaseEventResponse typedResponse = new TokenPurchaseEventResponse();
            typedResponse.purchaser = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.beneficiary = (Address) eventValues.getIndexedValues().get(1);
            typedResponse.value = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TokenPurchaseEventResponse> tokenPurchaseEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TokenPurchase", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TokenPurchaseEventResponse>() {
            @Override
            public TokenPurchaseEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                TokenPurchaseEventResponse typedResponse = new TokenPurchaseEventResponse();
                typedResponse.purchaser = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.beneficiary = (Address) eventValues.getIndexedValues().get(1);
                typedResponse.value = (Uint256) eventValues.getNonIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Future<Uint256> rate() {
        Function function = new Function("rate", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Uint256> weiRaised() {
        Function function = new Function("weiRaised", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> sendToken(Address _beneficiary, Uint256 valSend) {
        Function function = new Function("sendToken", Arrays.<Type>asList(_beneficiary, valSend), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> burn(Uint256 _burnVal) {
        Function function = new Function("burn", Arrays.<Type>asList(_burnVal), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> wallet() {
        Function function = new Function("wallet", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> renounceOwnership() {
        Function function = new Function("renounceOwnership", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> removeFromWhitelist(Address _beneficiary) {
        Function function = new Function("removeFromWhitelist", Arrays.<Type>asList(_beneficiary), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> addManyToWhitelist(DynamicArray<Address> _beneficiaries) {
        Function function = new Function("addManyToWhitelist", Arrays.<Type>asList(_beneficiaries), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Bool> whitelist(Address param0) {
        Function function = new Function("whitelist", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> addToWhitelist(Address _beneficiary) {
        Function function = new Function("addToWhitelist", Arrays.<Type>asList(_beneficiary), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> buyTokens(Address _beneficiary, BigInteger weiValue) {
        Function function = new Function("buyTokens", Arrays.<Type>asList(_beneficiary), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function, weiValue);
    }

    public Future<TransactionReceipt> transferOwnership(Address newOwner) {
        Function function = new Function("transferOwnership", Arrays.<Type>asList(newOwner), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> token() {
        Function function = new Function("token", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<MoaSaleCrowdsale> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 _rate, Address _wallet, Address _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_rate, _wallet, _token));
        return deployAsync(MoaSaleCrowdsale.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Future<MoaSaleCrowdsale> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, Uint256 _rate, Address _wallet, Address _token) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_rate, _wallet, _token));
        return deployAsync(MoaSaleCrowdsale.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static MoaSaleCrowdsale load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MoaSaleCrowdsale(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static MoaSaleCrowdsale load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MoaSaleCrowdsale(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OwnershipRenouncedEventResponse {
        public Address previousOwner;
    }

    public static class OwnershipTransferredEventResponse {
        public Address previousOwner;

        public Address newOwner;
    }

    public static class TokenPurchaseEventResponse {
        public Address purchaser;

        public Address beneficiary;

        public Uint256 value;

        public Uint256 amount;
    }
}
