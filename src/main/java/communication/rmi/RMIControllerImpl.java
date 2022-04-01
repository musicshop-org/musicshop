package communication.rmi;

import application.api.LoginService;
import application.LoginServiceImpl;
import application.api.SessionFacade;

import sharedrmi.application.dto.*;
import sharedrmi.communication.rmi.RMIController;

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
    public List<AlbumDTO> findAlbumsBySongTitle(String s) {
        return sessionFacade.findAlbumsBySongTitle(s);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String s) {
        return sessionFacade.findSongsByTitle(s);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String s) {
        return sessionFacade.findArtistsByName(s);
    }

    @Override
    public ShoppingCartDTO getCart() {
        return sessionFacade.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) {
        sessionFacade.addProductToCart(albumDTO, i);
    }

    @Override
    public void changeQuantity(LineItemDTO lineItemDTO, int i) {
        sessionFacade.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(LineItemDTO lineItemDTO) {
        sessionFacade.removeProductFromCart(lineItemDTO);
    }
}
