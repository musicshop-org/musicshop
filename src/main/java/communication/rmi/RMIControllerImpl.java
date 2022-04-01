package communication.rmi;

import application.LoginService;
import application.LoginServiceImpl;
import application.SessionFacade;

import communication.rmi.api.RMIController;
import sharedrmi.application.dto.*;

import javax.security.auth.login.FailedLoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIControllerImpl extends UnicastRemoteObject implements RMIController {

    private final SessionFacade sessionFacade;

    protected RMIControllerImpl(String username, String password) throws FailedLoginException, RemoteException {
        super();

        LoginService loginService = new LoginServiceImpl();
        this.sessionFacade = loginService.login(username, password);
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String s) throws RemoteException {
        return sessionFacade.findAlbumsBySongTitle(s);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String s) throws RemoteException {
        return sessionFacade.findSongsByTitle(s);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String s) throws RemoteException {
        return sessionFacade.findArtistsByName(s);
    }

    @Override
    public ShoppingCartDTO getCart() throws RemoteException {
        return sessionFacade.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) throws RemoteException {
        sessionFacade.addProductToCart(albumDTO, i);
    }

    @Override
    public void changeQuantity(LineItemDTO lineItemDTO, int i) throws RemoteException {
        sessionFacade.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(LineItemDTO lineItemDTO) throws RemoteException {
        sessionFacade.removeProductFromCart(lineItemDTO);
    }
}